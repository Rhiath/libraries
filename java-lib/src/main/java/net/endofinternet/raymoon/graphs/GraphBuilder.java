/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
public class GraphBuilder {

    private final List<Integer>[] edges;
    private int edgeCount;
    private final int vertexCount;

    public GraphBuilder(int vertexCount) {
        edges = new List[vertexCount];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new LinkedList<Integer>();            
        }
        this.vertexCount = vertexCount;
        this.edgeCount = 0;
    }

    public void addEdge(int vertexA, int vertexB) {
        edgeCount += 2;
        edges[vertexA].add(vertexB);
        edges[vertexB].add(vertexA);
    }

    public Graph getGraph() {
       return new GraphImpl(edges, edgeCount, vertexCount);
    }
}
