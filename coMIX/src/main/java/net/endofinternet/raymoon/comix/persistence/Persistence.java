/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.comix.persistence;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface Persistence {

    public VertexRef storeVertex(String classType, Object... properties);

    public void removeVertex(VertexRef reference);

    public void updateVertex(VertexRef reference, Object... properties);

    public List<String> getVertexProperties(VertexRef reference);

    public <T> T getVertexProperty(VertexRef reference, String propertyName);

    public EdgeRef storeEdge(String classType, VertexRef source, String attribute, VertexRef destination, Object... properties);

    public void removeEdge(EdgeRef reference);

    public void updateEdge(EdgeRef reference, Object... properties);

    public List<String> getEdgeProperties(EdgeRef reference);

    public <T> T getEdgeProperty(EdgeRef reference, String propertyName);

    public Iterable<VertexRef> getVertices(String classType, String propertName, String propertyValue);

    public Iterable<EdgeRef> getEdges(String classType, String propertName, String propertyValue);

    public static interface VertexRef {
    }

    public static interface EdgeRef {
    }
}
