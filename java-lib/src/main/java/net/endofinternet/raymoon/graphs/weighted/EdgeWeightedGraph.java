/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.weighted;

/**
 *
 * @author raymoon
 */
public interface EdgeWeightedGraph {

    public Iterable<WeightedEdge> getAdjacent(int vertex);

    public Iterable<WeightedEdge> getAllEdges();

    public int getNumberOfVertices();

    public int getNumberOfEdges();
}
