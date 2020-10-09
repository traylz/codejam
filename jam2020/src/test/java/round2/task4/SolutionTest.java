package round2.task4;

import org.junit.Test;

import static org.junit.Assert.*;

public class SolutionTest {
    @Test
    public void shouldSolveSample() throws Exception {
        final int result = Solution.solve("12 5\n" +
            "(()(((()))))\n" +
            "1 1 1 1 1 1 1 1 1 1 1 1\n" +
            "1 1 1 1 1 1 1 1 1 1 1 1\n" +
            "1 1 1 1 1 1 1 1 1 1 1 1\n" +
            "7 4 4 12 5\n" +
            "12 11 10 1 6");

        assertEquals(10, result);
    }
}