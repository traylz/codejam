package round1a.task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Solution {
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
            BigInteger result = solveNew(array);
            System.out.println("Case #" + i + ": " + result);
        }
    }

    public static BigInteger solveNew(Integer[][] rows) {
        Set<CellId> cellsToEliminate = new HashSet<>();
        Graph<CellId> horizontalGraph = new Graph<>();
        Graph<CellId> verticalGraph = new Graph<>();
        Map<CellId, Cell> cells = new HashMap<>();
        for (int rowId = 0; rowId < rows.length; rowId++) {
            Integer[] row = rows[rowId];
            for (int columnId = 0; columnId < row.length; columnId++) {
                int mastery = row[columnId];
                Cell cell = new Cell(mastery);
                CellId cellId = new CellId(rowId, columnId);
                if (rowId > 0) {
                    cell.addNeighbour(rows[rowId - 1][columnId]);
                    verticalGraph.addLink(cellId, new CellId(rowId - 1, columnId));
                }
                if (rowId < rows.length - 1) {
                    cell.addNeighbour(rows[rowId + 1][columnId]);
                    verticalGraph.addLink(cellId, new CellId(rowId + 1, columnId));
                }
                if (columnId > 0) {
                    cell.addNeighbour(rows[rowId][columnId - 1]);
                    horizontalGraph.addLink(cellId, new CellId(rowId, columnId - 1));
                }
                if (columnId < row.length - 1) {
                    cell.addNeighbour(rows[rowId][columnId + 1]);
                    horizontalGraph.addLink(cellId, new CellId(rowId, columnId + 1));
                }
                cells.put(cellId, cell);
                if (cell.shouldEliminate()) {
                    cellsToEliminate.add(cellId);
                }
            }
        }
        int round = 1;
        BigInteger sum = BigInteger.ZERO;
        while (!cellsToEliminate.isEmpty()) {
            Set<CellId> nextCellsToEliminate = new HashSet<>();
            Iterator<CellId> eliminationIterator = cellsToEliminate.iterator();
            while (eliminationIterator.hasNext()) {
                CellId cellToEliminate = eliminationIterator.next();
                Cell dancorToEliminate = cells.get(cellToEliminate);
                removeFromGraph(horizontalGraph, cells, nextCellsToEliminate, cellToEliminate, dancorToEliminate);

                removeFromGraph(verticalGraph, cells, nextCellsToEliminate, cellToEliminate, dancorToEliminate);

                dancorToEliminate.eliminate();
                sum = sum.add(BigInteger.valueOf(dancorToEliminate.mastery).multiply(BigInteger.valueOf(round)));

                eliminationIterator.remove();
            }
            cellsToEliminate = nextCellsToEliminate.stream().filter(it -> !cells.get(it).eliminated).collect(Collectors.toSet());
            round++;
        }
        long uneliminatedMastery = cells.values().stream().filter(cell -> !cell.eliminated).mapToLong(cell -> cell.mastery).sum();
        sum = sum.add(BigInteger.valueOf(uneliminatedMastery).multiply(BigInteger.valueOf(round)));
        return sum;
    }

    private static void removeFromGraph(Graph<CellId> graph, Map<CellId, Cell> cells, Set<CellId> nextCellsToEliminate, CellId cellToEliminate, Cell dancorToEliminate) {
        for (CellId neigbourToCheck : graph.getAdjacent(cellToEliminate)) {
            Cell neighbour = cells.get(neigbourToCheck);
            neighbour.removeNeighbour(dancorToEliminate);
            if (neighbour.shouldEliminate()) {
                nextCellsToEliminate.add(neigbourToCheck);
            } else {
                nextCellsToEliminate.remove(neigbourToCheck);
            }
        }
        graph.removeNodeWithRelink(cellToEliminate, (id1, id2) -> {
            Cell cell1 = cells.get(id1);
            Cell cell2 = cells.get(id2);
            cell1.addNeighbour(cell2.mastery);
            cell2.addNeighbour(cell1.mastery);
            if (cell1.shouldEliminate()) {
                nextCellsToEliminate.add(id1);
            } else {
                nextCellsToEliminate.remove(id1);
            }
            if (cell2.shouldEliminate()) {
                nextCellsToEliminate.add(id2);
            } else {
                nextCellsToEliminate.remove(id2);
            }
        });
    }

}


class Cell {
    final int mastery;
    int sumOfAdjacent;
    int numOfAdjacent;
    boolean eliminated = false;

    Cell(int mastery) {
        this.mastery = mastery;
    }

    boolean shouldEliminate() {
        if (numOfAdjacent == 0) {
            return false;
        }
        return sumOfAdjacent > mastery * numOfAdjacent;
    }

    public void removeNeighbour(Cell neighbour) {
        sumOfAdjacent -= neighbour.mastery;
        numOfAdjacent--;
    }

    public void addNeighbour(int mastery) {
        sumOfAdjacent += mastery;
        numOfAdjacent++;
    }

    public void eliminate() {
        eliminated = true;
    }
}

class CellId {
    final int row;
    final int column;

    CellId(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellId cellId = (CellId) o;
        return row == cellId.row &&
            column == cellId.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}

class Graph<ID> {

    Map<ID, Set<ID>> links = new HashMap<>();

    boolean addLink(ID one, ID two) {
        if (one.equals(two)) {
            return false;
        }
        links.computeIfAbsent(one, (__) -> new HashSet<>());
        links.computeIfAbsent(two, (__) -> new HashSet<>());
        links.get(one).add(two);
        return links.get(two).add(one);
    }

    Set<ID> getAdjacent(ID node) {
        Set<ID> adjacent = links.get(node);
        if (adjacent == null) {
            return Collections.emptySet();
        }
        return adjacent;
    }

    void removeNodeWithRelink(ID node, BiConsumer<ID, ID> doOnRelink) {
        Set<ID> adjacent = getAdjacent(node);
        links.remove(node);
        for (ID one : adjacent) {
            links.get(one).remove(node);
            for (ID two : adjacent) {
                if (one != two) {
                    if (addLink(one, two)) {
                        doOnRelink.accept(one, two);
                    }
                }
            }
        }
    }

}