package qualification.task3;

import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.*;
import static qualification.task3.Solution.Interval.parseFrom;

public class SolutionTest {

    @Test
    public void testCase1() {
        List<Solution.Person> solution = Solution.solve(List.of(
            parseFrom("99 150", 0),
            parseFrom("1 100", 1),
            parseFrom("100 301", 2),
            parseFrom("2 5", 3),
            parseFrom("150 250", 4)
        ));

        assertEquals("JCCJJ", solution.stream().map(Solution.Person::toString).collect(joining()));
    }
}





