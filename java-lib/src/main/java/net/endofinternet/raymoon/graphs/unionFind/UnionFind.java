/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.unionFind;

import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;



/**
 *
 * @author raymoon
 */
public interface UnionFind {

    public void union(int p, int q) throws InvalidContentException;

    public int find(int p) throws InvalidContentException;
}
