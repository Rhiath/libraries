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

    public void addUndirectedEdge(int vertexA, int vertexB) {
        addDirectedEdge(vertexA, vertexB);
        addDirectedEdge(vertexB, vertexA);
    }
    public void addDirectedEdge(int vertexA, int vertexB) {
        edgeCount += 1;
        edges[vertexA].add(vertexB);
    }

    public Graph getGraph() {
       return new GraphImpl(edges, edgeCount, vertexCount);
    }
}
