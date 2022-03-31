package com.example.simplify_debts;

import java.util.ArrayList;
import java.util.List;

public abstract class NetworkFlowSolverBase {

    // To avoid overflow
    protected static final long INF = Long.MAX_VALUE;

    public static class Edge {
        public int from, to;
        public String fromLabel, toLabel;
        public Edge residual;
        public long flow, cost;
        public final long capacity, originalCost;

        public Edge(int from, int to, long capacity) {
            this(from, to, capacity, 0);
        }

        public Edge(int from, int to, long capacity, long cost) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            originalCost = this.cost = cost;
        }

        public boolean isResidual() {
            return capacity == 0;
        }

        public long remainingCapacity() {
            return capacity - flow;
        }

        public void augment(long bottleNeck) {
            flow += bottleNeck;
            residual.flow -= bottleNeck;
        }

        public String toString(int s, int t) {
            String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
            String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
            return String.format("Edge %s -> %s | flow = %d | capacity = %d | is residual: %s", u, v, flow,
                    isResidual());
        }
    }
    // n = number of nodes, s = source, t = sink

    protected int n, s, t;

    protected long maxFlow;
    protected long minCost;

    protected boolean[] minCut;
    protected List<Edge>[] graph;
    protected String[] vertexLabels;
    protected List<Edge> edges;

    // if node i is visited: visited[i] = visitedToken;
    private int visitedToken = 1;
    private int[] visited;

    protected boolean solved;

    public NetworkFlowSolverBase(int n, String[] vertexLabels) {
        this.n = n;
        initGraph();
        assignLabeltoVertices(vertexLabels);
        minCut = new boolean[n];
        visited = new int[n];
        edges = new ArrayList<>();
    }

    private void initGraph() {
        graph = new List[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<Edge>();
    }

    // add labels(names) to vertices in the graph.
    private void assignLabeltoVertices(String[] vertexLabels) {
        if (vertexLabels.length != n) {
            throw new IllegalArgumentException(String.format("You must pass %s number of labels", n));
        }
        this.vertexLabels = vertexLabels;
    }

    // add a list of directed edges(and residual edges) to the graph
    // param edges : A list of all edges to be added to the flow graph.
    public void addEdges(List<Edge> edges) {
        if (edges == null)
            throw new IllegalArgumentException("Edges cannot be nul");
        for (Edge edge : edges) {
            addEdge(edge.from, edge.to, edge.capacity);
        }
    }

    // add a directed edge(and residual edges) to the flow graph.

    public void addEdge(int from, int to, long capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity<0");
        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0); // create reverse path
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
        edges.add(e1);
    }

    public void addEdge(int from, int to, long capacity, long cost) {
        Edge e1 = new Edge(from, to, capacity, cost);
        Edge e2 = new Edge(to, from, 0, -cost);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
        edges.add(e1);
    }

    //mark visited node
    public void visit(int i) {
        visited[i] = visitedToken;
    }

    public boolean visited(int i) {
        return visited[i] == visitedToken;
    }
    //change visitedToken
    public void markAllNodesUnvisited() {
        visitedToken++;
    }

    public List<Edge>[] getGraph(){
        execute();
        return graph;
    }

    public List<Edge> getEdges(){
        return edges;
    }
    //return the maxflow from the source to the sink.
    public long getMaxFlow() {
        execute();
        return maxFlow;
    }
    //only applies to min-cost max-flow algorithms.
    public long getMinCost() {
        execute();
        return minCost;
    }

    //S marked as true. T marked as false.
    //S:Vertices are reachable from s(include s).
    //T:Vertices are not Reachable from s(include t).
    public boolean[] getMinCut() {
        execute();
        return minCut;
    }

    public void setSource(int s) {
        this.s = s;
    }

    public void setSink(int t) {
        this.t = t;
    }
    public int getSource() {
        return s;
    }
    public int getSink() {
        return t;
    }
    //Set 'solved' flag to false to force recomputation of subsequent flows.
    public void recompute() {
        solved = false;
    }
    //print all edges.
    public void printEdges() {
        for(Edge edge : edges) {
            System.out.println(String.format("%s ----%s----> %s", vertexLabels[edge.from], edge.capacity, vertexLabels[edge.to]));
        }
    }
    //Wrapper method that ensures we only  call solve() once
    private void execute() {
        if(solved) return;
        solved = true;
        solve();
    }

    public abstract void solve();
}

