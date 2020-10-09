package round2.task2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class Solution {

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int numOfCases = Integer.parseInt(in.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            String result = solve(in);
            System.out.println("Case #" + i + ": " + result);
        }
    }

    // For testing
    static String solve(String aCase) throws Exception {
        return solve(new BufferedReader(new StringReader(aCase)));
    }

    private static String solve(BufferedReader caseReader) throws Exception {
        final String[] setup = caseReader.readLine().split(" ");
        int numOfNodes = Integer.parseInt(setup[0]);
        int numOfEdges = Integer.parseInt(setup[1]);
        List<Node> nodes = new ArrayList<>(numOfNodes);
        List<Edge<Node>> edges = new ArrayList<>(numOfEdges);
        nodes.add(new Node(1, 0));

        final String[] latencies = caseReader.readLine().split(" ");
        for (int i = 2; i <= latencies.length + 1; i++) {
            nodes.add(new Node(i, Integer.parseInt(latencies[i - 2])));
        }

        for (int i = 0; i < numOfEdges; i++) {
            String[] edge = caseReader.readLine().split(" ");
            final int fromNodeIdx = Integer.parseInt(edge[0]) - 1;
            final int toNodeIdx = Integer.parseInt(edge[1]) - 1;
            edges.add(new Edge<>(nodes.get(fromNodeIdx), nodes.get(toNodeIdx)));
        }
        return solve(nodes, edges);
    }


    private static String solve(List<Node> nodeList, List<Edge<Node>> adjacencyList) {
        nodeList.get(0).isProcessed = true;
        Graph<Node, Edge<Node>> graph = Graph.fromAdjacencyList(adjacencyList);
        // do BFS with two queues
        // will start with one, for first part
        Queue<Node> orderQ = new PriorityQueue<>(comparing(Node::order));
        Queue<Node> latencyQ = new PriorityQueue<>(comparing(Node::latency));
        orderQ.addAll(graph.getNeighbours(nodeList.get(0)).stream().filter(Node::hasOrderAssigned).collect(Collectors.toSet())); // start with 0 node
        latencyQ.addAll(graph.getNeighbours(nodeList.get(0)).stream().filter(Node::isLatency).collect(Collectors.toSet())); // start with 0 node

        int latencyHorizon = 0;
        int stepHorizon = 0;
        int sameStep = 0; // number of nodes already on same horizon
        boolean lastNodeWasPositioned = false;
        while (!orderQ.isEmpty() || !latencyQ.isEmpty()) {
            Node node;
            if (latencyQ.isEmpty() || (!orderQ.isEmpty() && (orderQ.peek().order() <= stepHorizon + sameStep + 1))) {
                node = orderQ.remove();
            } else if (!latencyQ.isEmpty()){
                node = latencyQ.remove();
            } else {
                throw new IllegalStateException();
            }
            if (node.isProcessed) {
                continue;
            }
            if (node.hasOrderAssigned()) { // it's position based-node
                if (stepHorizon + sameStep + 1 == node.order() || !lastNodeWasPositioned) { // ok, expand horizon
                    latencyHorizon++;
                    stepHorizon += (sameStep + 1);
                    sameStep = 0;
                } else if (stepHorizon == node.order()) {
                    sameStep++;
                } else {
                    throw new IllegalStateException();
                }
                node.latencyFrom0 = latencyHorizon;
                lastNodeWasPositioned = true;
            } else if (node.isLatency()) {
                if (node.latencyFrom0 == latencyHorizon) {
                    sameStep++;
                } else if (node.latencyFrom0 > latencyHorizon) { // ok, expand horizon
                    stepHorizon += (sameStep + 1);
                    sameStep = 0;
                    latencyHorizon = node.latencyFrom0;
                } else {
                    throw new IllegalStateException();
                }
                lastNodeWasPositioned = false;
            }
            node.isProcessed = true;
            graph.getNeighbours(node)
                .forEach(n -> {
                    if (!n.isProcessed) {
                        if (n.hasOrderAssigned()) {
                            orderQ.add(n);
                        } else {
                            latencyQ.add(n);
                        }
                    }
                });
        }

        return adjacencyList.stream().map(Solution::distance).map(Object::toString).collect(Collectors.joining(" "));
    }

    private static Integer distance(Edge<Node> edge) {
        final int distance = Math.abs(edge.from.latencyFrom0 - edge.to.latencyFrom0);
        return distance == 0 ? 1 : distance;
    }

}


class Edge<N> {
    final N from;
    final N to;

    Edge(N from, N to) {
        this.from = from;
        this.to = to;
    }

}


class Graph<N, E extends Edge<N>> {

    public static <N, E extends Edge<N>> Graph<N, E> fromAdjacencyList(List<E> adjacencyList) {

        final Graph<N, E> graph = new Graph<>();
        adjacencyList.forEach(graph::addEdge);
        return graph;
    }


    private final List<Edge<N>> edges = new ArrayList<>();

    void addEdge(E edge) {
        edges.add(edge);
    }

    Set<N> getNeighbours(N node) {
        return Stream.concat(
            edges.stream().filter(e -> e.from.equals(node)).map(e -> e.to),
            edges.stream().filter(e -> e.to.equals(node)).map(e -> e.from)
        ).collect(Collectors.toSet());

    }

}


class Node {
    final int id;
    int order;
    int latencyFrom0;
    boolean isProcessed = false;

    public Node(int i, int latency) {
        this.id = i;
        if (latency < 0) {
            order = -latency;
            latencyFrom0 = 0;
        } else {
            order = 0;
            latencyFrom0 = latency;
        }
    }


    public boolean isLatency() {
        return order < 0;
    }

    public boolean hasOrderAssigned() {
        return order > 0;
    }

    public int order() {
        if (!hasOrderAssigned()) {
            throw new IllegalStateException();
        }
        return order;
    }
    public int latency() {
        if (!isLatency()) {
            throw new IllegalStateException();
        }
        return latencyFrom0;
    }
}
