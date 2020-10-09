package round1c.task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class Solution {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int numOfCases = Integer.parseInt(in.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            final String[] setup = in.readLine().split(" ");
            final int numOfSlices = Integer.parseInt(setup[0]);
            final int numOfDiners = Integer.parseInt(setup[1]);
            final List<Q> slices = Stream.of(in.readLine().split(" ")).map(Long::parseLong).map(Q::integer).collect(Collectors.toList());
            int result = solve(numOfDiners, slices);
            System.out.println("Case #" + i + ": " + result);
        }
    }

    static int solve(int numOfDiners, List<Q> slices) {

        final TreeMap<Q, Integer> countsOfSlices = slices.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toMap(q -> q, q -> 1, Integer::sum, TreeMap::new));
        // Slice size -> [num_of_cuts, count]
        TreeMap<Q, TreeMap<Integer, Integer>> counts = new TreeMap<>();
        for (int i = 0; i < slices.size(); i++) {
            final Q slice = slices.get(0);
            for (int nd = 1; nd < numOfDiners; nd++) {
                final Q pieceSize = slice.divide(nd);
                counts
                    .putIfAbsent(pieceSize, new TreeMap<>());
                counts.get(pieceSize).compute(nd - 1, (__, curCnt) -> curCnt == null ? 1 : curCnt + 1);
            }
        }
        int minCutCount = numOfDiners - 1;
        for (Q pieceSize : counts.keySet()) {
            // take as much as we can for numOfDiners
            int numOfCuts = 0;
            int dinersLeftToFeed = numOfDiners;
            final TreeMap<Integer, Integer> numOfCutsToCount = counts.get(pieceSize);
            for (Map.Entry<Integer, Integer> cutsToCount : numOfCutsToCount.entrySet()) {
                int cutsHasToMakeForThatPiece = cutsToCount.getKey();
                int cutsMade = 0;
                while (dinersLeftToFeed > 0 && cutsMade < cutsToCount.getKey()) {
                    numOfCuts+=cutsHasToMakeForThatPiece;
                    cutsMade ++;
                    dinersLeftToFeed -= (cutsHasToMakeForThatPiece + 1);
                }
                if (dinersLeftToFeed <= 0) {
                    break;
                }
            }
            Q nextSlice = pieceSize;
            while (dinersLeftToFeed > 0 && (nextSlice = counts.higherKey(nextSlice)) != null) { // still have to feed people, even after cutting all divisible pieces
                if (!nextSlice.divide(pieceSize).denominator.equals(BigInteger.ONE)) {
                    Q sliceSizeLeft = nextSlice;
                    while (dinersLeftToFeed > 0 && sliceSizeLeft.compareTo(pieceSize) > 0) {
                        numOfCuts++;
                        dinersLeftToFeed--;
                        sliceSizeLeft = sliceSizeLeft.subtract(pieceSize);
                    }
                }
            }
            if (dinersLeftToFeed <= 0) {
                if (minCutCount > numOfCuts) {
                    minCutCount = numOfCuts;
                }
            }
        }
        return minCutCount;
    }

}


class Q implements Comparable<Q> {

    public static Q rational(long nominator, long denominator) {
        return new Q(BigInteger.valueOf(nominator), BigInteger.valueOf(denominator)).simplify();
    }

    public static Q integer(long value) {
        return new Q(BigInteger.valueOf(value), BigInteger.ONE).simplify();
    }

    BigInteger nominator;
    BigInteger denominator;

    Q(BigInteger nominator, BigInteger denominator) {
        this.nominator = nominator;
        this.denominator = denominator;
    }

    Q simplify() {
        if (denominator.signum() < 0) {
            denominator = denominator.negate();
            nominator = nominator.negate();
        }
        if (nominator.equals(ZERO)) {
            denominator = BigInteger.ONE;
        } else if (denominator.equals(ZERO)) {
            throw new IllegalStateException("Infinite number!");
        } else {
            BigInteger gcd = nominator.abs().gcd(denominator);
            while (!gcd.equals(ONE)) {
                nominator = nominator.divide(gcd);
                denominator = denominator.divide(gcd);
                gcd = nominator.abs().gcd(denominator.abs());
            }
        }
        return this;
    }

    Q neg() {
        return new Q(nominator.negate(), denominator).simplify();
    }

    Q inverted() {
        return new Q(denominator, nominator).simplify();
    }

    Q add(Q that) {
        return new Q(
            this.nominator.multiply(that.denominator).add(that.nominator.multiply(this.denominator)),
            this.denominator.multiply(that.denominator))
            .simplify();
    }

    Q multiply(Q that) {
        return new Q(
            this.nominator.multiply(that.nominator),
            this.denominator.multiply(that.denominator))
            .simplify();
    }

    Q divide(Q that) {
        return this.multiply(that.inverted()).simplify();
    }


    @Override
    public boolean equals(Object o) {
        Q that = (Q) o;
        return nominator.equals(that.nominator) &&
            denominator.equals(that.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominator, denominator);
    }

    @Override
    public int compareTo(Q that) {
        return this.nominator.multiply(that.denominator)
            .compareTo(
                that.nominator.multiply(this.denominator));
    }

    public Q divide(int nd) {
        return new Q(nominator, denominator.multiply(BigInteger.valueOf(nd))).simplify();
    }

    public Q subtract(Q that) {
        return add(that.neg()).simplify();
    }
}