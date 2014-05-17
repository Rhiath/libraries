/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.graphs.weighted;

import java.util.List;
import java.util.Random;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.OrderingComparison.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author raymoon
 */
public class MSTBuilderTest {

    @Test
    public void testPerformance() {
        for (int testRun = 0; testRun < 100; testRun++) {
            EdgeWeightedGraphBuilder builder = new EdgeWeightedGraphBuilder(1000);

            Random random = new Random();
            for (int i = 0; i < 5000; i++) {
                builder.addUndirectedEdge(random.nextInt(1000), random.nextInt(1000), random.nextDouble());
            }

            long t0 = System.currentTimeMillis();
            MSTBuilder.computeMST(builder.getGraph());
            long t1 = System.currentTimeMillis();
            Long timeSpent = Long.valueOf(t1 - t0);

            if (testRun > 10) {
                System.out.println(timeSpent + " msec passed for test #" + testRun);
                assertThat(timeSpent, lessThan(Long.valueOf(10)));
            } else {
                System.out.println("ignored test run (let JIT do its work): " + timeSpent + " msec passed for test #" + testRun);
            }
        }
    }

    @Test
    public void testCorrectness() {
        EdgeWeightedGraphBuilder builder = new EdgeWeightedGraphBuilder(5);

        builder.addUndirectedEdge(1, 2, 0.5);
        builder.addUndirectedEdge(0, 2, 1);
        builder.addUndirectedEdge(0, 1, 1);
        
        List<WeightedEdge> mst =  MSTBuilder.computeMST(builder.getGraph());
        
        assertThat(mst.size(), is(2));
        assertThat(mst, hasItem(new WeightedEdge(0, 1, 1)));
        assertThat(mst, hasItem(new WeightedEdge(1, 2, 0.5)));
    }
}
