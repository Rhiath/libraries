/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.weighted;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import net.endofinternet.raymoon.graphs.unionFind.UnionFind;
import net.endofinternet.raymoon.graphs.unionFind.WeightedQuickUnionWithPathCompression;

/**
 *
 * @author raymoon
 */
public class MSTBuilder {

    /**
     * computes the minimum spanning tree for a given graph using Kruskals
     * Algoritm. If there are multiple edges with the same weight, they are
     * sorted by the vertex indices (first by lower vertexID, then by higher
     * vertexID).
     *
     * @param graph the graph to compute the MST from
     * @return the list of vertices building the MST
     */
    public static List<WeightedEdge> computeMST(EdgeWeightedGraph graph) {
        List<WeightedEdge> mst = new LinkedList<WeightedEdge>();

        PriorityQueue<WeightedEdge> queue = new PriorityQueue<WeightedEdge>(1, buildEdgeComparator());

        for (WeightedEdge edge : graph.getAllEdges()) {
            queue.add(edge);
        }
        UnionFind unionFind = new WeightedQuickUnionWithPathCompression(graph.getNumberOfVertices());

        while (!queue.isEmpty() && mst.size() < graph.getNumberOfVertices() - 1) {
            WeightedEdge edge = queue.remove();
            int v = edge.either();
            int w = edge.other();
            if (unionFind.find(v) != unionFind.find(w)) {
                unionFind.union(v, w);
                mst.add(edge);
            }
        }

        return mst;
    }

    /**
     * builds an edge comparator which sorts the edges in ascending order. Edges
     * with the same weight are sorted by their vertex IDs (first by lower
     * vertexID, then by higher vertexID)
     *
     * sorts edges by weight ASC, min(either,other) ASC, max(either, other) ASC
     *
     * @return the comparator performing the described comparison
     */
    private static Comparator<? super WeightedEdge> buildEdgeComparator() {
        return new Comparator<WeightedEdge>() {
            @Override
            public int compare(WeightedEdge t, WeightedEdge t1) {
                int weightCompare = Double.valueOf(t.getWeight()).compareTo(Double.valueOf(t1.getWeight()));

                if (weightCompare == 0) {
                    int selfMin = Math.min(t.either(), t.other());
                    int selfMax = Math.max(t.either(), t.other());
                    int otherMin = Math.min(t1.either(), t1.other());
                    int otherMax = Math.max(t1.either(), t1.other());

                    int compareMin = Integer.valueOf(selfMin).compareTo(otherMin);
                    int compareMax = Integer.valueOf(selfMax).compareTo(otherMax);

                    if (compareMin == 0) {
                        return compareMax;
                    } else {
                        return compareMin;
                    }
                } else {
                    return weightCompare;
                }

            }
        };
    }
}
