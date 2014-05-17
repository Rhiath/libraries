/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.weighted;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
public class EdgeWeightedGraphBuilder {

    private final List<WeightedEdge>[] edges;
    private int edgeCount;
    private final int vertexCount;

    public EdgeWeightedGraphBuilder(int vertexCount) {
        edges = new List[vertexCount];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new LinkedList<WeightedEdge>();
        }
        this.vertexCount = vertexCount;
        this.edgeCount = 0;
    }

    public void addUndirectedEdge(int vertexA, int vertexB, double weight) {
        WeightedEdge edge = new WeightedEdge(vertexA, vertexB, weight);
        edges[vertexA].add(edge);
        edges[vertexB].add(edge);
        edgeCount++;
    }

    public EdgeWeightedGraph getGraph() {
        return new EdgeWeightedGraphImpl(edges, edgeCount, vertexCount);
    }
}
