/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest;

/**
 *
 * @author raymoon
 */
import java.security.Principal;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.MessageContext;

@Path("/calc")
public class CustomerService {

    @GET
    @Path("/add/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String addPlainText(@PathParam("a") double a, @PathParam("b") double b) {
        return (a + b) + "";
    }

    @GET
    @Path("/add/{a}/{b}")
    @Produces(MediaType.TEXT_XML)
    public String add(@PathParam("a") double a, @PathParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" + (a + b) + "</result>";
    }

    @GET
    @Path("/sub/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public String subPlainText(@PathParam("a") double a, @PathParam("b") double b) {
        return (a - b) + "";
    }

    @GET
    @Path("/sub/{a}/{b}")
    @Produces(MediaType.TEXT_XML)
    public String sub(@PathParam("a") double a, @PathParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" + (a - b) + "</result>";
    }

    @GET
    @Path("/ratings/{id}")
    public Response deleteRating(@PathParam("id") int id, @Context MessageContext context) {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());


        System.out.println(policy.getUserName());

        // code here: check if he is the owner of the rating
        // code here: deny the request or
        // code here: delete the rating
        return null;
    }
}
