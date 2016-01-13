package com.mkyong.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("be")
public class MessageRestService {

    @GET
    @Path("/{entityID}")
    public Response getBusinessEntity(@PathParam("entityID") String entityID) {
        String result = "getBusinessEntity : " + entityID;

        return Response.status(200).entity(result).build();
    }

    @POST
    @Path("/{entityID}")
    @Consumes()
    public Response putBusinessEntity(@PathParam("entityID") String entityID, String payload) {
        String result = "putEntity : " + entityID;

        return Response.status(200).entity(result).build();

    }

    @DELETE
    @Path("/take/{entityID}")
    public Response removeBusinessEntity(@PathParam("entityID") String entityID) {

        String result = "removeBusinessEntity : " + entityID;

        return Response.status(200).entity(result).build();

    }
}