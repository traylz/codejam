package round1b.task1;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class SolutionTest {
    @Test
    public void shouldReturnResult() {
        check(3, 0, "EE");
        check(-2, -3, "NWS");
        check(-2, 3, "SWN");
        check(2, -3, "NES");
        check(2, 3, "SEN");
        check(1, 4, "WEN");
        check(1, -4, "WES");
        check(-1, -4, "EWS");
        check(4, 4, "IMPOSSIBLE");
        check(2, 2, "IMPOSSIBLE");
        check(3, 3, "IMPOSSIBLE");
        check(-1, 4, "EWN");
    }

    private static void check(int x, int y, String expected) {
        final String solution = Solution.solve(x, y);
        assertEquals(expected, solution);

    }

    @Test
    public void seekTest() {
        final int[] seek = Solution.seek(BigInteger.valueOf(5), 3);
        assertArrayEquals(new int[]{-1, 1, 1}, seek);
    }
}