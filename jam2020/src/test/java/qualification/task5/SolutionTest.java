package qualification.task5;

import org.junit.Test;

import java.util.Optional;

public class SolutionTest {

    @Test
    public void should_solve() {
        for (int i = 2; i <= 50; i++) {
            for (int j = i; j <= i * i; j++) {
                check(i, j);
            }
        }
    }

    @Test
    public void should_solve_5_5() {
        check(5, 5);
    }

    private void check(int i, int j) {
        try {
            System.out.println("\n==== FOR " + i + "/" + j + "===\n");
            Optional<Matrix> result = Solution.solve(i, j);
            if (result.isPresent()) {
                System.out.println(result.get());
            } else {
                System.out.println("NO SOLUTION\n");
            }
        } catch (Exception e) {
            System.out.println("RUNTIME EXCEPTION\n");
        }

    }

}