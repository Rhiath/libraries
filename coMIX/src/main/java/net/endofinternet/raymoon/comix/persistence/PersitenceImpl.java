/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.comix.persistence;

import com.google.gson.Gson;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.List;
import net.endofinternet.raymoon.comix.persistence.businessEntity.Comic;
import net.endofinternet.raymoon.comix.persistence.businessEntity.File;

/**
 *
 * @author raymoon
 */
public class PersitenceImpl implements Persistence {

    private final OrientGraphFactory factory;

    public PersitenceImpl(OrientGraphFactory factory) {
        this.factory = factory;
    }

    public VertexRef storeVertex(String classType, Object... properties) {
        OrientGraph graph = factory.getTx();
        try {
            Vertex newLast = graph.addVertex(classType, properties);
            newLast.setProperty("__isReplicationLast", true);

            Vertex lastVertex = getLastVertex();
            if (lastVertex == null) {
                newLast.setProperty("__isReplicationFirst", true);
            } else {
                lastVertex.removeProperty("__isReplicationLast");
                graph.addEdge(null, lastVertex, newLast, "__replicationFollows");
            }

            return new VertexRefImpl(newLast.getId().toString());
        } finally {
            graph.shutdown();
        }

    }

    public void removeVertex(VertexRef reference) {
        OrientGraph graph = factory.getTx();
        try {
            Vertex current = graph.getVertex(((VertexRefImpl) reference).id);

            if (current.getPropertyKeys().contains("__isReplicationFirst")) {
                for (Edge e : current.getEdges(Direction.IN, "__replicationFollows")) {
                    e.getVertex(Direction.IN).setProperty("__isReplicationFirst", true);
                    graph.removeEdge(e);
                }
            }

            if (current.getPropertyKeys().contains("__isReplicationLast")) {
                for (Edge e : current.getEdges(Direction.OUT, "__replicationFollows")) {
                    e.getVertex(Direction.OUT).setProperty("__isReplicationLast", true);
                    graph.removeEdge(e);
                }
            }

            graph.removeVertex(current);
        } finally {
            graph.shutdown();
        }
    }

    public void updateVertex(VertexRef reference, Object... properties) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getVertexProperties(VertexRef reference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> T getVertexProperty(VertexRef reference, String propertyName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EdgeRef storeEdge(String classType, VertexRef source, String attribute, VertexRef destination, Object... properties) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeEdge(EdgeRef reference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateEdge(EdgeRef reference, Object... properties) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getEdgeProperties(EdgeRef reference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> T getEdgeProperty(EdgeRef reference, String propertyName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterable<VertexRef> getVertices(String classType, String propertName, String propertyValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Iterable<EdgeRef> getEdges(String classType, String propertName, String propertyValue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Vertex getLastVertex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class VertexRefImpl implements VertexRef {

        public String id;

        public VertexRefImpl(String id) {
            this.id = id;
        }
    }
}
