package round1a.task1;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SolutionTest {
    @Test
    public void should_find_prefix() {
        String solve = Solution.solve(List.of(
            "*CONUTS",
            "*COCONUTS",
            "*OCONUTS",
            "*CONUTS",
            "*S"
        ));
        assertEquals("COCONUTS", solve);
    }

    @Test
    public void should_return_null() {
        String solve = Solution.solve(List.of(
            "*XZ",
            "*XYZ"
        ));
        assertEquals("*", solve);


    }
    @Test
    public void should_return_middle_words() {
        String solve = Solution.solve(List.of(
            "*X*",
            "*Y*"
        ));
        assertEquals("XY", solve);
    }


}