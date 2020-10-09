package round2.task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;

import static java.util.Optional.empty;

public class Solution {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws IOException {
        final int numOfCases = Integer.parseInt(READER.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            int size = Integer.parseInt(READER.readLine());
            List<Molecule> molecules = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                String[] vals = READER.readLine().split(" ");
                molecules.add(new Molecule(Integer.parseInt(vals[0]), Integer.parseInt(vals[1])));
            }
            System.out.println("Case #" + i + ": " + solve(molecules));
        }
    }

    public static int solve(List<Molecule> molecules) {
        Set<Q> rateSet = new HashSet<>();
        for (int i = 0; i < molecules.size() - 1; i++) {
            for (int j = i + 1; j < molecules.size(); j++) {
                Molecule first = molecules.get(i);
                Molecule second = molecules.get(j);
                Optional<Q> rate = rateBetween(first, second);
                rate.ifPresent(rateSet::add);
            }
        }
        return rateSet.size() + 1;
    }

    private static Optional<Q> rateBetween(Molecule first, Molecule second) {
        if (first.codium == second.codium || first.jamium == second.jamium) {
            return empty();
        } else if ((first.codium > second.codium) == (first.jamium > second.jamium)) {
            return empty();
        }
        return Optional.of(Q.rational(first.codium - second.codium, second.jamium - first.jamium));
    }
}


class Molecule {
    final int codium;
    final int jamium;

    Molecule(int codium, int jamium) {
        this.codium = codium;
        this.jamium = jamium;
    }

}

class Q {

    public static Q rational(long nominator, long denominator) {
        return new Q(nominator, denominator).simplify();
    }

    long nominator;
    long denominator;

    Q(long nominator, long denominator) {
        this.nominator = nominator;
        this.denominator = denominator;
    }

    Q simplify() {
        if (denominator < 0) {
            denominator *= -1;
            nominator *= -1;
        }
        if (nominator == 0) {
            denominator = 1;
        } else if (denominator == 0) {
            throw new IllegalStateException("Infinite number!");
        } else {
            int gcd = BigInteger.valueOf(nominator).abs().gcd(BigInteger.valueOf(denominator)).intValueExact();
            while (gcd != 1) {
                nominator /= gcd;
                denominator /= gcd;
                gcd = BigInteger.valueOf(nominator).abs().gcd(BigInteger.valueOf(denominator).abs()).intValueExact();
            }
        }
        return this;
    }


    @Override
    public boolean equals(Object o) {
        Q that = (Q) o;
        return nominator == that.nominator &&
            denominator == that.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nominator, denominator);
    }

}
