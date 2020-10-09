package qualification.task4;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class SolutionTest {
    @Test
    public void should_solve() throws IOException {
        check("1100110011");
        check("0000000000");
        check("1111100000");
        check("1111100001");
        check("1110000001");
        check("1000000001");
        check("0000000001");
        check("0000011111");
        check("1111111111");
        check("1101101001");
        check("11011010011111101001");
        check("00000000000000000000");
        check("11011010011111111001");
        check("1100110011110011001111001100111100110011110011001111001100111100110011110011001111001100111100110011");
    }

    private void check(String str) throws IOException {
        Judge judge = new Judge(Judge.stringToBoolArray(str));
        Solution solution = new Solution(judge, judge.size());
        assertTrue(solution.run());

    }

}
