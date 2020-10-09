package round1c.task3;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static round1c.task3.Q.integer;

public class SolutionTest {
    @Test
    public void shouldSolve3() {
        final int solution = Solution.solve(3, List.of(integer(2L), integer(2L), integer(3L)));
        assertEquals(1, solution);
    }
    @Test
    public void shouldSolve3_2() {
        final int solution = Solution.solve(3, List.of(integer(3L), integer(3L), integer(2L)));
        assertEquals(2, solution);
    }
}