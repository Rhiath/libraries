/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.unionFind;

import net.endofinternet.raymoon.graphs.unionFind.UnionFind;
import net.endofinternet.raymoon.graphs.unionFind.WeightedQuickUnionWithPathCompression;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;
import org.hamcrest.CoreMatchers;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

/**
 *
 * @author raymoon
 */
public class UnionFindTest {

    @Test
    public void testConstructor() {
        WeightedQuickUnionWithPathCompression algorithm = new WeightedQuickUnionWithPathCompression(100);
    }

    @Test
    public void afterConstructionAllNodesHaveTheirOwnValue() throws InvalidContentException {
        int nodeCount = 100;
        UnionFind algorithm = new WeightedQuickUnionWithPathCompression(nodeCount);
        for (int i = 0; i < nodeCount; i++) {
            assertTrue(algorithm.find(i) == i);
        }
    }

    @Test
    public void connectedNodesHaveTheSameID() throws InvalidContentException {
        int nodeCount = 100;
        UnionFind algorithm = new WeightedQuickUnionWithPathCompression(nodeCount);
        algorithm.union(1, 3);
        algorithm.union(7, 2);
        algorithm.union(2, 3);
        algorithm.union(6, 7);

        assertTrue(algorithm.find(1) == algorithm.find(2));
        assertTrue(algorithm.find(1) == algorithm.find(3));
        assertTrue(algorithm.find(1) == algorithm.find(6));
        assertTrue(algorithm.find(1) == algorithm.find(7));
        assertTrue(algorithm.find(0) == 0);
        assertTrue(algorithm.find(4) == 4);
        assertTrue(algorithm.find(5) == 5);
        for (int i = 8; i < nodeCount; i++) {
            assertTrue(algorithm.find(i) == i);
        }
    }
}
