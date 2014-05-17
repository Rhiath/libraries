/*
 * 
 */
package net.endofinternet.raymoon.graphs.unionFind;

/**
 * Implementation of the UnionFind interface. The runtime characteristics are:
 * - constructor: O(size)
 * - union: O( lg*(size) )
 * - find: O( lg*(size) )
 *
 * Spoken in practical terms, the lg* function value is less or equal to 5:
 * lg*(2^65536)==5
 *
 * @author raymoon
 */
public class WeightedQuickUnionWithPathCompression implements UnionFind {

    private final int[] id;
    private final int[] depth;

    public WeightedQuickUnionWithPathCompression(int size) {
        id = new int[size];
        depth = new int[size];

        for (int i = 0; i < id.length; i++) {
            id[i] = i;
            depth[i] = 1;
        }
    }

    @Override
    public void union(int p, int q) {
        int idP = root(p);
        int idQ = root(q);
        if (depth[idP] < depth[idQ]) {
            id[idP] = idQ;
            depth[idQ] += depth[idP];
        } else {
            id[idQ] = idP;
            depth[idP] += depth[idQ];
        }
    }

    @Override
    public int find(int p) {
        return root(this.id[p]);
    }

    private int root(int p) {
        int lastIndex = p;
        while (lastIndex != id[lastIndex]) {
            id[lastIndex] = id[id[lastIndex]];
            lastIndex = id[lastIndex];
        }

        return lastIndex;
    }
}
