package round1a.task2;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SolutionTest {

    @Test
    public void solve() {
        validate(1000000000);
        validate(19);
        validate(4);
        validate(1);
    }

    private void validate(int desiredAmount) {
        List<Pair> result = Solution.solve(desiredAmount);
        long sum = result.stream().mapToLong(pair -> Solution.pascal(pair.k, pair.n)).sum();
        assertTrue(result.size() < 500);
        assertEquals(desiredAmount, sum);
    }

    @Test
    public void checkPascal() {
        assertEquals(Solution.pascal(1, 1), 1);
        assertEquals(Solution.pascal(1, 2), 1);
        assertEquals(Solution.pascal(1, 3), 1);
        assertEquals(Solution.pascal(2, 3), 2);
        assertEquals(Solution.pascal(2, 30), 29);
        assertEquals(Solution.pascal(4, 8), 35);
    }
}

/*
1 1 = 1
1 2 = 1
2 3 = 2
2 4 = 3
3 5 = 6
2 5 = 4
1 5 = 1
1 6 = 1

 */