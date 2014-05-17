/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.utils;

import net.endofinternet.raymoon.graphs.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author raymoon
 */
public class GraphUtilsTest {

    @Test
    public void testDegree() {
        GraphBuilder builder = new GraphBuilder(20);

        builder.addEdge(10, 12);
        builder.addEdge(10, 11);
        builder.addEdge(10, 13);

        Graph graph = builder.getGraph();
        assertThat(GraphUtils.degree(graph, 10), is(3));
        assertThat(GraphUtils.degree(graph, 11), is(1));
    }

    @Test
    public void testMaxDegree() {
        GraphBuilder builder = new GraphBuilder(20);

        builder.addEdge(10, 12);
        builder.addEdge(10, 11);
        builder.addEdge(10, 13);

        Graph graph = builder.getGraph();
        assertThat(GraphUtils.maxDegree(graph), is(3));
    }

    @Test
    public void testAverageDegree() {
        GraphBuilder builder = new GraphBuilder(4);

        builder.addEdge(1, 2);
        builder.addEdge(1, 1);
        builder.addEdge(1, 3);

        Graph graph = builder.getGraph();
        assertThat(GraphUtils.averageDegree(graph), is(6.0/4.0));
    }
}
