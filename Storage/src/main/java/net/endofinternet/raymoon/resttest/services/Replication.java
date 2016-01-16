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
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.query.OQuery;
import com.orientechnologies.orient.core.query.OQueryAbstract;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLQuery;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.core.tx.OTransaction;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.Id;
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

    public static class ReplicationSource {

        @OVersion
        private String version;

        @Id
        private ORID orid;

        private String node;
        private String baseURL;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public ORID getOrid() {
            return orid;
        }

        public void setOrid(ORID orid) {
            this.orid = orid;
        }

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public String getBaseURL() {
            return baseURL;
        }

        public void setBaseURL(String baseURL) {
            this.baseURL = baseURL;
        }

    }

    public static class ReplicationNotification {

        @OVersion
        private String version;

        @Id
        private ORID orid;

        private String node;
        private String id;

        private int priority;

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public ORID getOrid() {
            return orid;
        }

        public void setOrid(ORID orid) {
            this.orid = orid;
        }

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

    private final Thread t = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Replication.class.getName()).log(Level.SEVERE, null, ex);
                }
                try (OObjectDatabaseTx database = new OObjectDatabaseTx("remote:localhost/petshop").open("root", "root")) {
                    database.setAutomaticSchemaGeneration(true);
                    database.getEntityManager().registerEntityClass(ReplicationNotification.class);
                    database.getEntityManager().registerEntityClass(ReplicationSource.class);

                    boolean keepLooping = true;
                    while (keepLooping) {
                        List<ReplicationNotification> results = database.query(new OSQLSynchQuery<>("select from ReplicationNotification order by priority ASC limit 1 "));

                        if (results.isEmpty()) {
                            keepLooping = false;
                        } else {
                            handleNotification(results.get(0), database);
                        }

                        System.out.println("results count: " + results.size());
                    }
                }
            }
        }

        private void handleNotification(ReplicationNotification get, OObjectDatabaseTx database) {

            System.out.println("found notification for source '" + get.getId() + "'");
            System.out.println(get.getNode());
            System.out.println(get.getOrid());
            System.out.println(get.getVersion());
            System.out.println(get.getPriority());

            List<ReplicationSource> results = database.query(new OSQLSynchQuery<>("select from ReplicationSource where node = '" + get.getNode() + "' limit 1"));

            database.begin();

            if (results.isEmpty()) {
                System.out.println("no source library known for '" + get.getNode() + "'");
                get.setPriority(1);
                database.save(get);
            } else {
                System.out.println("found a source library");
                System.out.println(results.get(0).getBaseURL());

                // TODO store local copy
                database.delete(get.orid);
            }

            database.commit();
        }

    };

    public void start() {
        t.start();
    }

    @GET
    @Path("v1_notify/{storageNode}/{id}")
    public Response notify(@PathParam("storageNode") String node, @PathParam("id") String id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        ReplicationNotification notification = new ReplicationNotification();
        notification.setId(id);
        notification.setNode(node);
        notification.setPriority(0);

        try (OObjectDatabaseTx database = new OObjectDatabaseTx("remote:localhost/petshop").open("root", "root")) {
            database.getEntityManager().registerEntityClass(ReplicationNotification.class);

            database.begin();
            database.getTransaction().setIsolationLevel(OTransaction.ISOLATION_LEVEL.READ_COMMITTED);

            database.save(notification);

            database.commit();
        }

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
