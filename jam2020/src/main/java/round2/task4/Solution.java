package round2.task4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptySet;
import static java.util.Comparator.comparingLong;

public class Solution {

    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final int numOfCases = Integer.parseInt(in.readLine());
        for (int i = 1; i <= numOfCases; i++) {
            int result = solve(in);
            System.out.println("Case #" + i + ": " + result);
        }
    }


    static int solve(String aCase) throws Exception {
        return solve(new BufferedReader(new StringReader(aCase)));
    }

    private static int solve(BufferedReader caseReader) throws Exception {
        final String[] setup = caseReader.readLine().split(" ");
        int programLen = Integer.parseInt(setup[0]);
        int numOfQueries = Integer.parseInt(setup[1]);
        String program = caseReader.readLine();
        final int[] leftCost = Stream.of(caseReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        final int[] rightCost = Stream.of(caseReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        final int[] matchingCost = Stream.of(caseReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < programLen; i++) {
            nodes.add(new Node(
                program.charAt(i) == '(',
                rightCost[i],
                leftCost[i],
                matchingCost[i]));
        }


        final int[] fromIndexes = Stream.of(caseReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        final int[] toIndexes = Stream.of(caseReader.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        int result = 0;
        for (int i = 0; i < numOfQueries; i++) {
            Node fromNode = nodes.get(fromIndexes[i] - 1);
            Node toNode = nodes.get(toIndexes[i] - 1);
            final int solutionForQuery = solve(nodes, fromNode, toNode);
            if (solutionForQuery == -1) {
                throw new IllegalStateException();
            }
            result += solutionForQuery;
        }
        return result;
    }



    public static int solve(List<Node> nodes, Node from, Node to) {
        Graph<Node> graph = collectToGraph(nodes);

        return (int) graph.shortestPath(from, to).stream().mapToLong(e -> e.weight).sum();

    }

    private static Graph<Node> collectToGraph(List<Node> nodes) {
        Graph<Node> graph = new Graph<>();

        Deque<Node> stack = new ArrayDeque<>();
        Node prevNode = nodes.get(0);
        stack.push(prevNode);
        for (int i = 1; i < nodes.size(); i++) {
            Node curr = nodes.get(i);
            graph.addEdge(curr, prevNode, curr.priceLeft);
            graph.addEdge(prevNode, curr, prevNode.priceRight);
            if (curr.opening) {
                stack.push(curr);
            } else {
                Node matching = stack.pop();
                graph.addEdge(curr, matching, curr.priceMatching);
                graph.addEdge(matching, curr, matching.priceMatching);
            }
            prevNode = curr;
        }
        return graph;
    }
}

class Node {
    final boolean opening;
    final int priceRight;
    final int priceLeft;
    final int priceMatching;

    Node(boolean opening, int priceRight, int priceLeft, int priceMatching) {
        this.opening = opening;
        this.priceRight = priceRight;
        this.priceLeft = priceLeft;
        this.priceMatching = priceMatching;
    }
}


class Graph<N> {

    class Edge {
        public final N from;
        public final N to;
        public final long weight;

        Edge(N from, N to, long weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return from.equals(edge.from) &&
                to.equals(edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    private Map<N, Set<Edge>> outboundEdges = new HashMap<>();

    public void addEdge(N from, N to, long weight) {
        outboundEdges.computeIfAbsent(from, __ -> new HashSet<>());

        final Set<Edge> outgoingEdgesCollection = outboundEdges.get(from);
        Edge edge = new Edge(from, to, weight);
        Optional<Edge> existingEdge = outgoingEdgesCollection.stream().filter(e -> e.equals(edge)).findAny();
        if (existingEdge.isPresent() && existingEdge.get().weight < weight) {
            return;
        }
        outgoingEdgesCollection.add(edge);
    }

    public Set<Edge> edgesFrom(N node) {
        return outboundEdges.getOrDefault(node, emptySet());
    }

    public long shortestDist(N from, N to) {
        if (from == to) {
            return 0;
        }
        final List<Edge> shortestPath = shortestPath(from, to);
        if (shortestPath.isEmpty()) {
            return -1;
        }
        return shortestPath.stream().mapToLong(e -> e.weight).sum();
    }

    public List<Edge> shortestPath(N from, N to) {
        Set<N> processed = new HashSet<>();

        Queue<NodeWithDistance> q = new PriorityQueue<>(comparingLong(nwd -> nwd.distance));

        q.add(nodeWithDist(from, 0, null, null));
        while (!q.isEmpty()) {
            NodeWithDistance node = q.remove(); // if a node pops up here, we have reached it via shortest path
            if (processed.contains(node.node)) {
                continue; //already processed with smaller distance, so just skipping here
            }
            if (node.node.equals(to)) {
                // ok we're done! let's traverse back up
                List<Edge> path = new LinkedList<>(); // will insert to beginning
                while (node.prev != null) {
                    path.add(0, node.prevEdge);
                    node = node.prev;
                }
                return path;
            }
            Set<Edge> edges = edgesFrom(node.node).stream().filter(e -> !processed.contains(e.to)).collect(Collectors.toSet());
            for (Edge edge : edges) {
                long dist = edge.weight + node.distance;
                q.add(nodeWithDist(edge.to, dist, node, edge));
            }
            processed.add(node.node);
        }
        return Collections.emptyList(); // unreachable if there is path
    }

    private NodeWithDistance nodeWithDist(N node, long dist, NodeWithDistance incomingNode, Edge prevEdge) {
        return new NodeWithDistance(node, dist, incomingNode, prevEdge);
    }

    private class NodeWithDistance {
        final N node;
        final long distance;
        final NodeWithDistance prev;
        final Edge prevEdge;

        private NodeWithDistance(N node, long distance, NodeWithDistance prev, Edge prevEdge) {
            this.node = node;
            this.distance = distance;
            this.prev = prev;
            this.prevEdge = prevEdge;
        }
    }
}