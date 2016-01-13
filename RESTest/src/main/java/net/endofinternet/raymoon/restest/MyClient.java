/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.restest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 *
 * @author raymoon
 */
public class MyClient {

    public static void main(String[] args) {
        try {

            Client client = Client.create();
            client.addFilter(new HTTPBasicAuthFilter("Ray", "pwd"));

            WebResource webResource = client
                    .resource("http://localhost:9000/businessEntity/v1.0/stationA");

//            for (int i = 0; i < 1000; i++) {
            ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }
//long t0 = System.nanoTime();
            String output = response.getEntity(String.class);
//                long t1 = System.nanoTime();
//                System.out.println((t1-t0)+"msec");
//            }

            System.out.println("Output from Server .... \n");
            System.out.println(output);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
