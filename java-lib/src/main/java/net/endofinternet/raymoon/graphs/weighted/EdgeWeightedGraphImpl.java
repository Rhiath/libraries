/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.weighted;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author raymoon
 */
class EdgeWeightedGraphImpl implements EdgeWeightedGraph {

    private final List<WeightedEdge>[] edges;
    private int edgeCount;
    private final int vertexCount;

    public EdgeWeightedGraphImpl(List<WeightedEdge>[] edges, int edgeCount, int vertexCount) {
        this.edges = edges;
        this.edgeCount = edgeCount;
        this.vertexCount = vertexCount;
    }

    @Override
    public Iterable<WeightedEdge> getAdjacent(int vertex) {
        return edges[vertex];
    }

    @Override
    public Iterable<WeightedEdge> getAllEdges() {
        Set<WeightedEdge> retValue = new HashSet<WeightedEdge>();
        for (List<WeightedEdge> edgelist : edges ){
            for ( WeightedEdge edge : edgelist){
                retValue.add(edge);
            }
        }
        
        return retValue;
    }

    @Override
    public int getNumberOfVertices() {
       return vertexCount;
    }

    @Override
    public int getNumberOfEdges() {
        return edgeCount;
    }
}
