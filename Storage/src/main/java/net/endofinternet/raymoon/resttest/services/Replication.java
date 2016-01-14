/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest.services;

/**
 *
 * @author raymoon
 */
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

    @GET
    @Path("/v1_notify/{id}")
    public Response notify(@PathParam("storageNode") String node, @PathParam("id") String id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        // TODO
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
