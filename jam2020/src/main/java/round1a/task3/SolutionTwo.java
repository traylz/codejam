package round1a.task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SolutionTwo {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int numOfCases = Integer.parseInt(in.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            String[] sizes = in.readLine().split(" ");
            int rowNum = Integer.parseInt(sizes[0]);
            int columnNum = Integer.parseInt(sizes[1]);
            Integer[][] array = new Integer[rowNum][columnNum];
            for (int j = 0; j < rowNum; j++) {
                String[] vals = in.readLine().split(" ");
                for (int k = 0; k < columnNum; k++) {
                    array[j][k] = Integer.parseInt(vals[k]);
                }
            }
            long result = solve(array);
            System.out.println("Case #" + i + ": " + result);
        }
    }

    public static long solve(Integer[][] rows) {
        long sum = 0;
        boolean hasToContinue = true;
        while (hasToContinue) {
            sum += sumOver(rows);
            hasToContinue = false;
            for (int rowId = 0; rowId < rows.length; rowId++) {
                Integer[] row = rows[rowId];
                for (int columnId = 0; columnId < row.length; columnId++) {
                    Integer thisMastery = row[columnId];
                    if (thisMastery != null) {
                        int numOfNeighbours = 0;
                        int masteryOfNeighbours = 0;
                        Integer leftMastery = leftMastery(rows, rowId, columnId);
                        if (leftMastery != null) {
                            masteryOfNeighbours += leftMastery;
                            numOfNeighbours++;
                        }
                        Integer rightMastery = rightMastery(rows, rowId, columnId);
                        if (rightMastery != null) {
                            masteryOfNeighbours += rightMastery;
                            numOfNeighbours++;
                        }
                        Integer bottomMastery = bottomMastery(rows, rowId, columnId);
                        if (bottomMastery != null) {
                            masteryOfNeighbours += bottomMastery;
                            numOfNeighbours++;
                        }
                        Integer topMastery = topMastery(rows, rowId, columnId);
                        if (topMastery != null) {
                            masteryOfNeighbours += topMastery;
                            numOfNeighbours++;
                        }
                        if (numOfNeighbours > 0 && thisMastery * numOfNeighbours < masteryOfNeighbours) {
                            hasToContinue = true;
                            row[columnId] = -thisMastery; // mark for elimination
                        }
                    }
                }
            }

            for (int rowId = 0; rowId < rows.length; rowId++) {
                Integer[] row = rows[rowId];
                for (int columnId = 0; columnId < row.length; columnId++) {
                    Integer thisMastery = row[columnId];
                    if (thisMastery != null && thisMastery < 0) { // if marked for elimination
                        row[columnId] = null;
                    }
                }
            }

        }
        return sum;
    }

    private static Integer topMastery(Integer[][] rows, int rowId, int columnId) {
        for (int row = rowId - 1; row >= 0 ; row--) {
            Integer value = rows[row][columnId];
            if (value != null) {
                return value > 0 ? value : -value;
            }
        }
        return null;
    }

    private static Integer bottomMastery(Integer[][] rows, int rowId, int columnId) {
        for (int row = rowId + 1; row < rows.length ; row++) {
            Integer value = rows[row][columnId];
            if (value != null) {
                return value > 0 ? value : -value;
            }
        }
        return null;
    }

    private static Integer rightMastery(Integer[][] rows, int rowId, int columnId) {
        for (int column = columnId + 1; column < rows[rowId].length ; column++) {
            Integer value = rows[rowId][column];
            if (value != null) {
                return value > 0 ? value : -value;
            }
        }
        return null;
    }

    private static Integer leftMastery(Integer[][] rows, int rowId, int columnId) {
        for (int column = columnId - 1; column >= 0 ; column--) {
            Integer value = rows[rowId][column];
            if (value != null) {
                return value > 0 ? value : -value;
            }
        }
        return null;

    }

    private static int sumOver(Integer[][] rows) {
        int sum = 0;
        for (int rowId = 0; rowId < rows.length; rowId++) {
            Integer[] row = rows[rowId];
            for (int columnId = 0; columnId < row.length; columnId++) {
                Integer mastery = row[columnId];
                if (mastery != null) {
                    sum += mastery;
                }
            }
        }
        return sum;
    }

}


