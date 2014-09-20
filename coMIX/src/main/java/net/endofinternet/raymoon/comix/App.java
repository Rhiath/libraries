package net.endofinternet.raymoon.comix;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        OrientGraphFactory factory = new OrientGraphFactory("plocal:graph/db").setupPool(1, 10);
        OrientGraph graph = factory.getTx();
        try {
            OrientVertex v1 = graph.addVertex("class:Customer", "name", "Jill", "age", 33, "city", "Rome", "born", "Victoria, TX");;
            OrientVertex v2 = graph.addVertex("class:Customer", "name", "Jay", "age", 33, "city", "Rome", "born", "Victoria, TX");;
            

            graph.commit();

            for (Vertex v : graph.getVertices("Customer.name", "Jay")) {
                System.out.println("Found vertex: " + v);
            }

        } finally {
            graph.shutdown();
        }

    }
}
