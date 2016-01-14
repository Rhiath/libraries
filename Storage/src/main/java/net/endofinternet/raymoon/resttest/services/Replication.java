/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest.services;

/**
 *
 * @author raymoon
 */
import com.orientechnologies.orient.core.annotation.OVersion;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.tx.OTransaction;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import javax.ws.rs.core.Context;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.MessageContext;

import javax.ws.rs.core.Response;

@Path("/replication")
public class Replication {

    public class ReplicationNotification {

        @OVersion
        private String version;
        private String node;
        private String id;

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @GET
    @Path("v1_notify/{storageNode}/{id}")
    public Response notify(@PathParam("storageNode") String node, @PathParam("id") String id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        ReplicationNotification notification = new ReplicationNotification();
        notification.setId(id);
        notification.setNode(node);

        OObjectDatabaseTx database = new OObjectDatabaseTx("remote:localhost/petshop").open("root", "root");
        database.getEntityManager().registerEntityClass(ReplicationNotification.class);

        database.begin();
        database.getTransaction().setIsolationLevel(OTransaction.ISOLATION_LEVEL.READ_COMMITTED);

        database.save(notification);

        database.commit();

        return Response.ok().build();
    }

    @GET
    @Path("/v1_get/{id}")
    public Response get(@PathParam("id") String id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        // TODO
        return Response.ok(new Object(), MediaType.APPLICATION_OCTET_STREAM_TYPE).build();
    }

}
