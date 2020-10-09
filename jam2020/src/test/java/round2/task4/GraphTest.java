package round2.task4;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void shortestPath() {
        Object node1 = new Object();
        Object node2 = new Object();
        Object node3 = new Object();
        Object node4 = new Object();
        Graph<Object> graph = new Graph<>();
        graph.addEdge(node1, node2, 10);
        assertEquals(10, graph.shortestDist(node1, node2));

        graph.addEdge(node1, node3, 100);
        assertEquals(100, graph.shortestDist(node1, node3));

        graph.addEdge(node2, node3, 10);
        assertEquals(20, graph.shortestDist(node1, node3));

        assertEquals(-1, graph.shortestDist(node1, node4));

        graph.addEdge(node3, node4, 10000);
        assertEquals(10020, graph.shortestDist(node1, node4));

    }


}