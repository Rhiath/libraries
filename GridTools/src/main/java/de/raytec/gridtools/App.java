package de.raytec.gridtools;

import de.raytec.gridtools.messaging.DecoderHelper;
import de.raytec.gridtools.messaging.ReceiverEndpointAnnouncement;
import de.raytec.gridtools.remoteOperations.MemoryStorage;
import de.raytec.java.lib.config.Config;
import de.raytec.java.lib.config.ConfigBuilder;
import de.raytec.java.lib.config.InvalidValueContentException;
import de.raytec.java.lib.config.NoSuchKeyException;
import de.raytec.java.lib.exceptions.InvalidContentException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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


        DatagramSocket receiverSocket = new DatagramSocket(9000);
        DatagramPool receiverPacketPool = new DatagramPool(10, 1500);
        ExecutorService receiverExecutor = new ThreadPoolExecutor(4, 4, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        Receiver receiver = new Receiver(receiverSocket, receiverExecutor, receiverPacketPool, buildHandler(sender));


        final byte[] localReceiverAddress = receiverSocket.getLocalAddress().getAddress(); // TODO fix determining the local network address
        Thread selfAnnouncer = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    byte[] data = new byte[8];
                    ByteBuffer buffer = ByteBuffer.wrap(data);
                    buffer.order(ByteOrder.BIG_ENDIAN); // network byte order
                    buffer.put((byte) 1); // protocol version 1
                    buffer.put((byte) 1); // command "Receiver available on address ..."
                    buffer.put(localReceiverAddress); // IPv4 address
                    buffer.putShort((short) 9000); // port ...

                    sender.sendAsynch(data, new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255}, 9000);
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
                if (DecoderHelper.decoderMatchesMessage(ReceiverEndpointAnnouncement.getDecoder(), packet.getData())) {
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
