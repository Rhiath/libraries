package net.endofinternet.raymoon.comix;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Parameter;
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

//            for (int i = 0; i < 1000; i++) {
//                OrientVertex v1 = graph.addVertex("class:Customer", "name", "Jill" + i, "age", 33, "city", "Rome", "born", "Victoria, TX");;
//                OrientVertex v2 = graph.addVertex("class:Customer", "name", "Jay" + i, "age", 33, "city", "Rome", "born", "Victoria, TX");;
//            }

//            graph.commit();
//            graph.dropIndex("Customer.name");
//            graph.createKeyIndex("name", Vertex.class, new Parameter("class", "Customer"));

            Vertex v1 = graph.addVertex(null);
            Vertex v2 = graph.addVertex(null);

            graph.addEdge(null, v1, v2, "owns");

            for (Edge e : v2.getEdges(Direction.BOTH, "owns")) {
                System.out.println(e.getVertex(Direction.OUT).getId() + " --( " + e.getLabel() + " )--> " + e.getVertex(Direction.IN).getId());
            }
            //            for (Vertex v : graph.getVertices("Customer.name", "Jay")) {
            //                System.out.println("Found vertex: " + v);
            //            }
        } finally {
            graph.shutdown();
        }

    }
}
