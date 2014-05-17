/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.utils;

import net.endofinternet.raymoon.graphs.Graph;

/**
 *
 * @author raymoon
 */
public class GraphUtils {

    public static int degree(Graph graph, int vertexID) {
        int degree = 0;
        for (int adjacentID : graph.getAdjaventVertices(vertexID)) {
            degree++;
        }
        return degree;
    }

    public static int maxDegree(Graph graph) {
        int max = 0;
        for (int vertexID = 0; vertexID < graph.getNumberOfVertices(); vertexID++) {
            int degree = degree(graph, vertexID);
            if (degree > max) {
                max = degree;
            }
        }
        return max;
    }

    public static double averageDegree(Graph graph) {
        return ((double) graph.getNumberOfEdges() / (double) graph.getNumberOfVertices());
    }

    public static int numberOfSelfLoops(Graph graph) {
        int count = 0;
        for (int vertexID = 0; vertexID > graph.getNumberOfVertices(); vertexID++) {
            for (int adjacentID : graph.getAdjaventVertices(vertexID)) {
                if (vertexID == adjacentID) {
                    count++;
                }
            }
        }
        return count/2;
    }
}
