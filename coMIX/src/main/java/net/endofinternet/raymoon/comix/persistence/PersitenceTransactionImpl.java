/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.comix.persistence;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
public class PersitenceTransactionImpl implements PersistenceTransaction {

    private final OrientGraph graph;

    public PersitenceTransactionImpl(OrientGraphFactory factory) {
        this.graph = factory.getTx();
    }

    public VertexRef storeVertex(String classType, Object... properties) {
        Vertex newLast = graph.addVertex(classType, properties);
        newLast.setProperty("__dbid", getNextReplicationPosition(graph));
        newLast.setProperty("__replicationPos", newLast.getProperty("__dbid"));
        newLast.setProperty("__version", 0);

        return new VertexRefImpl(newLast.getId().toString());
    }

    public void removeVertex(VertexRef reference) {
        Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
        String dbid = current.getProperty("__dbid");
        long version = current.getProperty("__version");
        for (String key : current.getPropertyKeys()) {
            current.removeProperty(key);
        }
        current.setProperty("__dbid", dbid);
        current.setProperty("__replicationPos", getNextReplicationPosition(graph));
        current.setProperty("__version", version + 1);
    }

    public void updateVertex(VertexRef reference, Object... properties) {
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
    }

    public List<String> getVertexProperties(VertexRef reference) {
        Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
        List<String> retValue = new LinkedList<String>();
        retValue.addAll(current.getPropertyKeys());

        retValue.remove("__dbid");
        retValue.remove("__replicationPos");
        retValue.remove("__version");

        return retValue;
    }

    public <T> T getVertexProperty(VertexRef reference, String propertyName) {
        Vertex current = graph.getVertex(((VertexRefImpl) reference).id);
        return current.getProperty(propertyName);
    }

    public EdgeRef storeEdge(String classType, VertexRef source, String attribute, VertexRef destination, Object... properties) {
        Vertex sourceVertex = graph.getVertex(((VertexRefImpl) source).id);
        Vertex destinationVertex = graph.getVertex(((VertexRefImpl) destination).id);

        Edge edge = graph.addEdge(classType, sourceVertex, destinationVertex, attribute);
        for (int i = 0; i < properties.length; i += 2) {
            edge.setProperty((String) properties[i], properties[i + 1]);
        }

        edge.setProperty("__dbid", getNextReplicationPosition(graph));
        edge.setProperty("__replicationPos", edge.getProperty("__dbid"));
        edge.setProperty("__version", 0);

        return new EdgeRefImpl((String) edge.getId());
    }

    public void removeEdge(EdgeRef reference) {
        Edge current = graph.getEdge(((EdgeRefImpl) reference).id);
        String dbid = current.getProperty("__dbid");
        long version = current.getProperty("__version");
        for (String key : current.getPropertyKeys()) {
            current.removeProperty(key);
        }
        current.setProperty("__dbid", dbid);
        current.setProperty("__replicationPos", getNextReplicationPosition(graph));
        current.setProperty("__version", version + 1);
    }

    public void updateEdge(EdgeRef reference, Object... properties) {
        Edge current = graph.getEdge(((EdgeRefImpl) reference).id);
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
    }

    public List<String> getEdgeProperties(EdgeRef reference) {
        Edge current = graph.getEdge(((EdgeRefImpl) reference).id);
        List<String> retValue = new LinkedList<String>();
        retValue.addAll(current.getPropertyKeys());

        retValue.remove("__dbid");
        retValue.remove("__replicationPos");
        retValue.remove("__version");

        return retValue;
    }

    public <T> T getEdgeProperty(EdgeRef reference, String propertyName) {
        Edge current = graph.getEdge(((EdgeRefImpl) reference).id);
        return current.getProperty(propertyName);
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

    public void dispose() {
        graph.shutdown();
    }

    private static class VertexRefImpl implements VertexRef {

        public String id;

        public VertexRefImpl(String id) {
            this.id = id;
        }
    }

    private static class EdgeRefImpl implements EdgeRef {

        public String id;

        public EdgeRefImpl(String id) {
            this.id = id;
        }
    }
}
