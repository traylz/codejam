package round1a.task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int numOfCases = Integer.parseInt(in.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            int desiredSum = Integer.parseInt(in.readLine());
            List<Pair> solution = solve(desiredSum);
            System.out.println("Case #" + i + ":");
            solution.forEach(System.out::println);
        }
    }

    public static List<Pair> solve(int desiredAmount) {
        List<Pair> path = new ArrayList<>();
        Pair nextStep = new Pair(1, 1);
        path.add(nextStep);
        int amountLeft = desiredAmount - 1;
        while (amountLeft != 0) {
            nextStep = chooseNextStep(nextStep, amountLeft);
            amountLeft -= pascal(nextStep.k, nextStep.n);
            path.add(nextStep);
        }
        return path;
    }

    private static Pair chooseNextStep(Pair currentPosition, int amountLeft) {
        // try to make step in following preferred order:
        // down right
        int k = currentPosition.k;
        int n = currentPosition.n;
        long sumUpToBottomLeft = sumUpTo(k, n + 1);
        if ((k + 1) * 2 <= n + 2) { // can step down right
            long sumUpToDownRight = sumUpToBottomLeft + pascal(k + 1, n + 1);
            if (sumUpToDownRight <= amountLeft) {
                return new Pair(k + 1, n + 1);
            }
        }
        // down left
        if (sumUpToBottomLeft <= amountLeft) {
            return new Pair(k, n + 1);
        }
        // pure left
        if (k == 1) {
            throw new IllegalStateException("WUT");
        }
        return new Pair(k - 1, n);
    }

    private static long sumUpTo(int k, int n) {
        long sum = 0;
        for (int i = 1; i <= k; i++) {
            sum += pascal(i, n);
        }
        return sum;
    }

    public static long pascal(int k, int n) {
        if (k == 1 || k == n) {
            return 1;
        }
        return Choose.choose(k - 1, n - 1);
    }

}

class Pair {
    final int k;
    final int n;

    Pair(int k, int n) {
        this.k = k;
        this.n = n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return k == pair.k &&
            n == pair.n;
    }

    @Override
    public int hashCode() {
        return Objects.hash(k, n);
    }

    @Override
    public String toString() {
        return n + " " + k;
    }
}

// Choose n of k with memoization
class Choose {
    private static final TreeMap<Integer, BigInteger> factorials = new TreeMap<>();

    public static long choose(int k, int N) {
        return factorial(N).divide(factorial(k).multiply(factorial(N - k))).longValueExact();
    }

    public static BigInteger factorial(int n) {
        Map.Entry<Integer, BigInteger> closestCalulatedFactorial = factorials.floorEntry(n);
        BigInteger result;
        int start;
        if (closestCalulatedFactorial != null) {
            start = closestCalulatedFactorial.getKey();
            result = closestCalulatedFactorial.getValue();
        } else {
            start = 1;
            result = BigInteger.ONE;
        }
        for (int i = start; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));

        }
        return result;
    }

}
