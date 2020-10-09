package round2.task1;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SolutionTest {

    public static final Random RANDOM = new Random(0);

    @Test
    public void solveSimple() {
        assertEquals("1 1 1", Solution.solveSimple(1, 2));
        assertEquals("2 1 0", Solution.solveSimple(2, 2));
        assertEquals("5 0 4", Solution.solveSimple(8, 11));
        assertEquals("60 44 26", Solution.solveSimple(800, 1100));
    }

    @Test
    public void solveHard() {
        assertEquals("1 1 1", Solution.solveHard(1, 2));
        assertEquals("2 1 0", Solution.solveHard(2, 2));
        assertEquals("5 0 4", Solution.solveHard(8, 11));
        assertEquals("60 44 26", Solution.solveHard(800, 1100));
        assertEquals("1414213561 1 1234742859", Solution.solveHard(1, 1000000000000000000L));
        assertEquals("1414213561 1234742859 1", Solution.solveHard(1000000000000000000L, 1L));
        assertEquals("1999999999 0 1000000000", Solution.solveHard(1000000000000000000L, 1000000000000000000L));
        assertEquals(Solution.sum(1999999999),  1000000000000000000L + 1000000000000000000L - 1000000000L);
    }

    @Test
    public void checkSame() {
        for (int i = 0; i < 1000; i++) {
            final int left = RANDOM.nextInt(100000) + 1;
            final int right = RANDOM.nextInt(100000) + 1;
            assertEquals(Solution.solveSimple(left, right), Solution.solveHard(left, right));
        }

    }

    @Test
    public void firstApproach() {
        for (int i = 0; i < 100000; i++) {
            assertEquals(Solution.findFirstApproach(i), Solution.findFirstApproachBinary(i));
        }
    }

    @Test
    public void checkSameCanEat() {
        for (int i = 0; i < 1000; i++) {
            final int left = RANDOM.nextInt(100000) + 1;
            final int right = RANDOM.nextInt(100000) + 1;
            assertEquals(Solution.findNumCanTake2(left, right), Solution.findNumCanTake2Binary(left, right));
        }

    }

}