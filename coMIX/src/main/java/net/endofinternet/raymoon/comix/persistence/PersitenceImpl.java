/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.comix.persistence;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.LinkedList;
import java.util.List;

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
            newLast.setProperty("__dbid", getNextReplicationPosition(graph));
            newLast.setProperty("__replicationPos", newLast.getProperty("__dbid"));
            newLast.setProperty("__version", 0);


            return new VertexRefImpl(newLast.getId().toString());
        } finally {
            graph.shutdown();
        }

    }

    public void removeVertex(VertexRef reference) {
        OrientGraph graph = factory.getTx();
        try {
            Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
            String dbid = current.getProperty("__dbid");
            long version = current.getProperty("__version");
            for (String key : current.getPropertyKeys()) {
                current.removeProperty(key);
            }
            current.setProperty("__dbid", dbid);
            current.setProperty("__replicationPos", getNextReplicationPosition(graph));
            current.setProperty("__version", version + 1);
        } finally {
            graph.shutdown();
        }
    }

    public void updateVertex(VertexRef reference, Object... properties) {
        OrientGraph graph = factory.getTx();
        try {
            Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
            String dbid = current.getProperty("__dbid");
            long version = current.getProperty("__version");
            for (String key : current.getPropertyKeys()) {
                current.removeProperty(key);
            }

            for (int i = 0; i < properties.length; i += 2) {
                current.setProperty((String) properties[i], properties[i + 1]);
            }

            current.setProperty("__dbid", dbid);
            current.setProperty("__replicationPos", getNextReplicationPosition(graph));
            current.setProperty("__version", version + 1);
        } finally {
            graph.shutdown();
        }
    }

    public List<String> getVertexProperties(VertexRef reference) {
        OrientGraph graph = factory.getTx();
        try {
            Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
            List<String> retValue = new LinkedList<String>();
            retValue.addAll(current.getPropertyKeys());

            retValue.remove("__dbid");
            retValue.remove("__replicationPos");
            retValue.remove("__version");

            return retValue;
        } finally {
            graph.shutdown();
        }
    }

    public <T> T getVertexProperty(VertexRef reference, String propertyName) {
        OrientGraph graph = factory.getTx();
        try {
            Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
            return current.getProperty(propertyName);
        } finally {
            graph.shutdown();
        }
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

    private long getNextReplicationPosition(OrientGraph graph) {
        long nextID = 0;

        for (Vertex v : graph.getVerticesOfClass("replicationCounter")) {
            nextID = v.getProperty("lastPosition");
            nextID++;
            v.setProperty("lastPosition", nextID);
        }

        if (nextID == 0) {
            graph.addVertex("replicationCounter", "lastPosition", 0);
        }

        return nextID;
    }

    private static class VertexRefImpl implements VertexRef {

        public String id;

        public VertexRefImpl(String id) {
            this.id = id;
        }
    }
}
