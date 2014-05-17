/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
public class GraphImpl implements Graph {

    private final List<Integer>[] edges;
    private final int edgeCount;
    private final int vertexCount;

    public GraphImpl(List<Integer>[] edges, int edgeCount, int vertexCount) {
        this.edges = copy(edges);
        this.edgeCount = edgeCount;
        this.vertexCount = vertexCount;
    }

    @Override
    public Iterable<Integer> getAdjaventVertices(int vertexID) {
        return edges[vertexID];
    }

    @Override
    public int getNumberOfVertices() {
        return vertexCount;
    }

    @Override
    public int getNumberOfEdges() {
        return edgeCount;
    }

    private List<Integer>[] copy(List<Integer>[] edges) {
        List<Integer>[] retValue = (List<Integer>[]) Array.newInstance(List.class, edges.length);
        
        for (int i = 0; i < edges.length; i++) {
           retValue[i] = new LinkedList<Integer>(edges[i]);
        }
        
        return retValue;
    }
}
