package de.raytec.gridtools;

import de.raytec.gridtools.remoteOperations.MemoryStorage;
import de.raytec.java.lib.config.Config;
import de.raytec.java.lib.config.ConfigBuilder;
import de.raytec.java.lib.config.InvalidValueContentException;
import de.raytec.java.lib.config.NoSuchKeyException;
import de.raytec.java.lib.logging.Logging;
import de.raytec.java.lib.transmission.TransmissionException;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException, TransmissionException, NoSuchKeyException, InvalidValueContentException {
        Config logConfig = ConfigBuilder.buildConfig(new File("etc/log4j.properties"));
        Logging.configure(logConfig);

        DatagramSocket senderSocket = new DatagramSocket(9001);
        DatagramPool senderPacketPool = new DatagramPool(50, 1500);
        ExecutorService senderExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Sender sender = new Sender(senderExecutor, senderPacketPool, senderSocket);

        DatagramSocket receiverSocket = new DatagramSocket(9000);
        DatagramPool receiverPacketPool = new DatagramPool(10, 1500);
        ExecutorService receiverExecutor = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Receiver receiver = new Receiver(receiverSocket, receiverExecutor, receiverPacketPool, buildHandler(sender));
        receiver.run();
    }

    private static PacketHandler buildHandler(final Sender sender) {
        return new PacketHandler() {
            public void handle(Packet packet) {
                for (int i = 0; i < 30; i++) {
                    sender.sendAsynch(new byte[]{1,2,3,4,5,6,7,8,9}, new byte[]{127, 0, 0, 1}, 8000);
                    System.out.println("sendoing outbound #"+i);
                }
            }
        };
    }
}
