package round2.task3;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;
import static round2.task3.Point.p;

public class SolutionTest {

    @Test
    public void solve() {
        final int result = Solution.solve(Set.of(
            p(0, 0),
            p(1, 1),
            p(2, 1),
            p(3, 1),
            p(8, 2),
            p(11, 2),
            p(14, 2)
            ));

        assertEquals(7, result);

    }
    @Test
    public void solve2() {
        final int result = Solution.solve(Set.of(
            p(0, 0),
            p(1, 1),
            p(2, 1),
            p(3, 1),
            p(8, 2),
            p(11, 2),
            p(14, 2),
            p(1000000000, -1000000000)
            ));

        assertEquals(8, result);

    }
}