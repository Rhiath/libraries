/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs;

/**
 *
 * @author raymoon
 */
public interface Graph {

    Iterable<Integer> getAdjaventVertices(int vertexID);

    int getNumberOfVertices();

    int getNumberOfEdges();
}
