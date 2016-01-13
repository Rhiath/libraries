/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest;

import javax.ws.rs.core.Response;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;

/**
 *
 * @author raymoon
 */
public class AuthenticationHandler implements RequestHandler {
 
    public Response handleRequest(Message m, ClassResourceInfo resourceClass) {
        AuthorizationPolicy policy = (AuthorizationPolicy)m.get(AuthorizationPolicy.class);
        if ( policy == null ){
            return Response.status(401).header("WWW-Authenticate", "Basic").build();
        }
        String username = policy.getUserName();
        String password = policy.getPassword();
        if (isAuthenticated(username, password)) {
            // let request to continue
            return null;
        } else {
            // authentication failed, request the authetication, add the realm name if needed to the value of WWW-Authenticate
            return Response.status(401).header("WWW-Authenticate", "Basic").build();
        }
    }

    private boolean isAuthenticated(String username, String password) {
       return "Ray".equals(username) && "pwd".equals(password);
    }
 
}
