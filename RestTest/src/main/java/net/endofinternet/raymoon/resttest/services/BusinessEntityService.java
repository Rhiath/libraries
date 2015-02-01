/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest.services;

/**
 *
 * @author raymoon
 */
import java.security.Principal;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.MessageContext;

@Path("/businessEntity/v1.0")
public class BusinessEntityService {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public String getBusinessEntity(@PathParam("id") String id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        System.out.println(policy.getUserName());
        
        return "payload(id)";
    }

    @PUT
    @Path("/{id}")
    public void setBusinessEntity(@PathParam("id") String id, @FormParam("payload") String payload, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        System.out.println(policy.getUserName());
        
        // TODO store business entity
    }

    @DELETE
    @Path("/{id}")
    public void deleteBusinessEntity(@PathParam("id") String id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        System.out.println(policy.getUserName());
        
        // TODO delete business entity
    }
}
