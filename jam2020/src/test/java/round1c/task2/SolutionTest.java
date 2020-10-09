package round1c.task2;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class SolutionTest {
    @Test
    public void shouldSolve() throws IOException {
        final BufferedReader input = new BufferedReader(new InputStreamReader(
            this.getClass().getResourceAsStream("/round1c/task2/input.txt")));
        final String result = Solution.solveProbabilistic(input);
        assertEquals("TPFOXLUSHB", result);
    }
}