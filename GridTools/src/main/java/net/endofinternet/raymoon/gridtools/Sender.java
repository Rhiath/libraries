/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools;

import net.endofinternet.raymoon.gridtools.messaging.Message;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.lib.documentation.SoftwarePattern;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;
import net.endofinternet.raymoon.lib.logging.Logging;

/**
 *
 * @author raymoon
 */
public class Sender {

    private final ExecutorService executor;
    private final DatagramPool resources;
    private final DatagramSocket socket;

    public Sender(ExecutorService executor, DatagramPool resources,  DatagramSocket socket) {
        this.executor = executor;
        this.resources = resources;
        this.socket = socket;
    }
    
    @SoftwarePattern(name = "WrapperFacade", roles = "?")
    public void sendAsynch(Message message, final byte[] address, final int port) throws InvalidContentException{
        sendAsynch(message.getEncoder().encode(message), address, port);
    }

    public synchronized void sendAsynch(final byte[] data, final byte[] address, final int port) {
        final DatagramPacket packet = resources.getNextPacket();

        executor.submit(new Runnable() {
            public void run() {
                try {
                    packet.setData(data);
                    packet.setLength(data.length);
                    packet.setPort(port);
                    packet.setAddress(Inet4Address.getByAddress(address));
                  
                    synchronized( socket) {
                        socket.send(packet);
                    }
                    Logging.debug(this.getClass(), "send package to "+packet.getAddress().getHostAddress()+":"+packet.getPort());
                    
                    resources.freePacket(packet);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
