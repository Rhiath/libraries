/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools;

import de.raytec.java.lib.logging.Logging;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raymoon
 */
public class Receiver {

    private final DatagramSocket receiverSocket;// = new DatagramSocket(9000);
    private final ExecutorService receiverExecutor;// = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private final DatagramPool receiverPacketPool;// = new DatagramPool(10, 1500);
    private final PacketHandler handler;
    
    
    public Receiver(DatagramSocket receiverSocket, ExecutorService receiverExecutor, DatagramPool receiverPacketPool, PacketHandler handler) {
        this.receiverSocket = receiverSocket;
        this.receiverExecutor = receiverExecutor;
        this.receiverPacketPool = receiverPacketPool;
        this.handler = handler;
    }

    public void run() {
        while (true) {
            DatagramPacket packet = receiverPacketPool.getNextPacket();
            Logging.debug(App.class, "waiting for new data");
            try {
                receiverSocket.receive(packet);

                final Packet packetWrapper = new Packet(receiverPacketPool, packet);

                receiverExecutor.submit(new Runnable() {

                    public void run() {
                        handler.handle(packetWrapper);
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
