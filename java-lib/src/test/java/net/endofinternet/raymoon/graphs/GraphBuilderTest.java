/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author raymoon
 */
public class GraphBuilderTest {

    @Test
    public void testInitialisation() {
        GraphBuilder builder = new GraphBuilder(100);
        Graph graph = builder.getGraph();
        assertThat(graph.getNumberOfVertices(), is(100));
    }

    @Test
    public void testEdges() {
        GraphBuilder builder = new GraphBuilder(100);
        
        builder.addEdge(10, 12);
        
        Graph graph = builder.getGraph();
        assertThat(graph.getNumberOfEdges(), is(2));
        assertThat(graph.getAdjaventVertices(10), everyItem(is(Integer.valueOf(12))));
        assertThat(graph.getAdjaventVertices(12), everyItem(is(Integer.valueOf(10))));
    }
}
