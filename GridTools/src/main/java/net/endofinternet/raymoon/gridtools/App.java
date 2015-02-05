package net.endofinternet.raymoon.gridtools;

import net.endofinternet.raymoon.gridtools.messaging.DecoderHelper;
import net.endofinternet.raymoon.gridtools.messaging.ReceiverEndpointAnnouncement;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.lib.config.Config;
import net.endofinternet.raymoon.lib.config.ConfigBuilder;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;
import net.endofinternet.raymoon.lib.config.NoSuchKeyException;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;
import net.endofinternet.raymoon.lib.logging.Logging;
import net.endofinternet.raymoon.lib.transmission.TransmissionException;

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
        final Sender sender = new Sender(senderExecutor, senderPacketPool, senderSocket);


        final DatagramSocket receiverSocket = new DatagramSocket(9000);
        DatagramPool receiverPacketPool = new DatagramPool(10, 1500);
        ExecutorService receiverExecutor = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Receiver receiver = new Receiver(receiverSocket, receiverExecutor, receiverPacketPool, buildHandler(sender));


        final Inet4Address address = (Inet4Address) receiverSocket.getLocalAddress(); // TODO fix determining the local network address
        Thread selfAnnouncer = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ReceiverEndpointAnnouncement announcment = new ReceiverEndpointAnnouncement(address, (short) receiverSocket.getLocalPort());
                    try {
                        sender.sendAsynch(announcment, new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}, 9000);
                    } catch (InvalidContentException ex) {
                        Logging.warn(this.getClass(), "failed to send self-announcment over the network", ex);
                    }
                }
            }
        };
        selfAnnouncer.setDaemon(true);
        selfAnnouncer.start();


        receiver.run();
    }

    private static PacketHandler buildHandler(final Sender sender) {
        return new PacketHandler() {
            public void handle(Packet packet) {
                if (DecoderHelper.decoderMatchesPacket(ReceiverEndpointAnnouncement.getDecoder(), packet)) {
                    try {
                        ReceiverEndpointAnnouncement announcement = DecoderHelper.decodeMessage(packet.getData(), ReceiverEndpointAnnouncement.getDecoder());
                        System.out.println("received 'ReceiverEndpointAnnouncement' from " + announcement.getAddress().getHostAddress() + ":" + announcement.getPort());
                    } catch (InvalidContentException ex) {
                        Logging.warn(this.getClass(), "failed to decode message", ex);
                    }
                }
            }
        };
    }
}
