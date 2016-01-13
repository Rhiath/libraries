/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest.services;

/**
 *
 * @author raymoon
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("/")
public class BusinessEntityService {
//
//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public String getBusinessEntity(@PathParam("id") String id, @Context MessageContext context) {
//        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());
//
//        System.out.println(policy.getUserName());
//
//        return "payload(id)";
//    }

    @GET
    @Path("/get")
    public Response get_streamed2(@Context MessageContext context) throws FileNotFoundException {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());
        final File file = new File("/home/raymoon/Pictures/DSC_4277.jpg");

        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM).build();
    }

    @GET
    @Path("/get_s")
    public Response get_streamed(@Context MessageContext context) throws FileNotFoundException {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());
        final File file = new File("/home/raymoon/Pictures/DSC_4277.bmp");
        StreamingOutput streamer = new StreamingOutput() {
            @Override
            public void write(final OutputStream output) throws IOException, WebApplicationException {
                
                try (InputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int read = 0;
                
                while (read >= 0 ) {
                    read = fis.read(buffer);
                    
                    if ( read > 0 ){
                        System.out.println("writing "+read+" bytes");
                        output.write(buffer, 0, read);
                        Thread.sleep(1);
                    
                    }
                }
                
                } catch (InterruptedException ex) {
                    Logger.getLogger(BusinessEntityService.class.getName()).log(Level.SEVERE, null, ex);
                }
                final FileChannel inputChannel = new FileInputStream(file).getChannel();
                final WritableByteChannel outputChannel = Channels.newChannel(output);
                try {
                    inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                } finally {
                    // closing the channels
                    inputChannel.close();
                    outputChannel.close();
                }
            }
        };
        
        return Response.ok(streamer, MediaType.APPLICATION_OCTET_STREAM).build();
    }

    @GET
    @Path("/get/{id}")
    public Response get(@PathParam("id") String id, @Context MessageContext context) throws FileNotFoundException {
        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());

        System.out.println(policy.getUserName());
        File file = new File("/home/raymoon/Pictures/DSC_4277.bmp");
        return Response.ok(new FileInputStream(file)).build();
    }
//
//    @PUT
//    @Path("/{id}")
//    public void setBusinessEntity(@PathParam("id") String id, @FormParam("payload") String payload, @Context MessageContext context) {
//        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());
//
//        System.out.println(policy.getUserName());
//
//        // TODO store business entity
//    }
//
//    @DELETE
//    @Path("/{id}")
//    public void deleteBusinessEntity(@PathParam("id") String id, @Context MessageContext context) {
//        AuthorizationPolicy policy = (AuthorizationPolicy) context.get(AuthorizationPolicy.class.getCanonicalName());
//
//        System.out.println(policy.getUserName());
//
//        // TODO delete business entity
//    }
}
