package com.example.simplify_debts;


import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static final long OFFSET = 1000000000L;
    private static Set<Long> visitedEdges; // Set won't have the same objects.

//    public static void main(String[] args) {
//        createGraph();
//
//    }

    public static Intent createGraph(String[] person, int debts, ArrayList<Integer>giver, ArrayList<Integer>taker, ArrayList<Integer> money) {

        int n = person.length;

        Dinics solver = new Dinics(n, person);
        solver = addAllTransactions(debts,solver,giver,taker,money);

        System.out.println();
        System.out.println("Simplifying Debts...");
        System.out.println("--------------------");
        System.out.println();

        visitedEdges = new HashSet<>();
        Integer edgePos;

        while ((edgePos = getNonVisitedEdge(solver.getEdges())) != null) {
            solver.recompute();
            Dinics.Edge firstEdge = solver.getEdges().get(edgePos);
            solver.setSource(firstEdge.from);
            solver.setSink(firstEdge.to);
            List<Dinics.Edge>[] residualGraph = solver.getGraph();
            List<Dinics.Edge> newEdges = new ArrayList<>();

            for (List<Dinics.Edge> allEdges : residualGraph) {
                for (Dinics.Edge edge : allEdges) {
//					long remainingFlow = ((edge.flow < 0) ? edge.capacity : (edge.capacity - edge.flow));
                    long remainingFlow;
                    // in solve(), edge.capacity didn't change.
                    if (edge.flow < 0) {
                        remainingFlow = edge.capacity;
                    } else
                        remainingFlow = edge.remainingCapacity();

                    if (remainingFlow > 0) {
                        newEdges.add(new Dinics.Edge(edge.from, edge.to, remainingFlow));
                    }
                }

            }
            // won't trigger solve()
            long maxFlow = solver.getMaxFlow();

            int source = solver.getSource();
            int sink = solver.getSink();
            visitedEdges.add(getHashKey(source, sink));
            solver = new Dinics(n, person);

            solver.addEdges(newEdges);
            // add an edge (u,v) with weight as max-flow to the residual graph.
            solver.addEdge(source, sink, maxFlow);
        }
        Intent intent;
        intent = solver.printEdges();
        System.out.println();

        return intent;
    }

    private static Dinics addAllTransactions(int debts,Dinics solver,ArrayList<Integer>giver,ArrayList<Integer>taker,ArrayList<Integer>money) {

        for(int i =0;i<debts;i++){
            solver.addEdge(giver.get(i),taker.get(i),money.get(i));
        }
        // Transactions made by Bob
        //solver.addEdge(1, 2, 40);
        // Transactions made by Charlie
        //solver.addEdge(2, 3, 20);
        // Transactions made by David
        //solver.addEdge(3, 4, 50);
        // Transactions made by Fred
        //solver.addEdge(5, 1, 10);
        //solver.addEdge(5, 2, 30);
        //solver.addEdge(5, 3, 10);
        //solver.addEdge(5, 4, 10);
        // Transactions made by Gabe
        //solver.addEdge(6, 1, 30);
        //solver.addEdge(6, 3, 10);
        return solver;
    }

    private static Integer getNonVisitedEdge(List<Dinics.Edge> edges) {
        Integer edgePos = null;
        int curEdge = 0;
        for (Dinics.Edge edge : edges) {
            if (!visitedEdges.contains(getHashKey(edge.from, edge.to))) {
                edgePos = curEdge;
            }
            curEdge += 1;
        }
        return edgePos;
    }

    private static Long getHashKey(int u, int v) {
        return u * OFFSET + v;
    }

}
