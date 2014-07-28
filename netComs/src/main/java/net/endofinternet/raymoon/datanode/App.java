package net.endofinternet.raymoon.datanode;

import com.barchart.udt.net.NetServerSocketUDT;
import com.barchart.udt.net.NetSocketUDT;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.datanode.messages.MessageHandlerImpl;
import net.endofinternet.raymoon.datanode.messages.NoCommonProtocolStackException;
import net.endofinternet.raymoon.datanode.messages.ProtocolDenominator;
import net.endofinternet.raymoon.datanode.protocolHandlers.LoopingProtocolHandler;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        startServiceProvider(9000);
        startServiceConsumer(9001, new InetSocketAddress("localhost", 9000));
    }

    private static void startServiceProvider(int port) throws IOException {
        final NetServerSocketUDT acceptorSocket = new NetServerSocketUDT();
        acceptorSocket.bind(new InetSocketAddress("localhost", port), 256);

        final ProtocolHandlerFactory factory = buildFactory();
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final Socket clientSocket = acceptorSocket.accept();

                        // Start the read ahead background task
                        Executors.newSingleThreadExecutor().submit(new Callable<Boolean>() {
                            @Override
                            public Boolean call() {
                                try (InputStream is = clientSocket.getInputStream()) {
                                    try (OutputStream os = clientSocket.getOutputStream()) {
                                        try {
                                            return serviceTask(is, os, factory);
                                        } catch (InvalidMessageTypeException ex) {
                                            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (ClassNotFoundException ex) {
                                            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (NoCommonProtocolStackException ex) {
                                            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                return true;
                            }
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    private static void startServiceConsumer(int port, SocketAddress remote) throws IOException {
        final NetSocketUDT acceptorSocket = new NetSocketUDT();
        acceptorSocket.bind(new InetSocketAddress("0.0.0.0", port));

        final ProtocolHandlerFactory factory = buildFactory();

        acceptorSocket.connect(remote);

        // Start the read ahead background task
        try (InputStream is = acceptorSocket.getInputStream()) {
            try (OutputStream os = acceptorSocket.getOutputStream()) {
                clientTask(is, os, factory);
            } catch (InvalidMessageTypeException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoCommonProtocolStackException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean serviceTask(InputStream is, OutputStream os, ProtocolHandlerFactory protocolHandlerFactory) throws IOException, InvalidMessageTypeException, ClassNotFoundException, NoCommonProtocolStackException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        MessageHandler messageHandler = new MessageHandlerImpl(ois, oos);
        messageHandler.writeMessage(protocolHandlerFactory.getSupportedProtocols());
        SupportedProtocols supportedRemotely = messageHandler.getMessage(SupportedProtocols.class);

        SupportedProtocols appliedProtocol = ProtocolDenominator.getCommonDenominator(protocolHandlerFactory.getSupportedProtocols(), supportedRemotely);

        protocolHandlerFactory.createHandler(appliedProtocol).handle(messageHandler);
        System.out.println("end of service task");

        return true;
    }

    public static boolean clientTask(InputStream is, OutputStream os, ProtocolHandlerFactory protocolHandlerFactory) throws IOException, InvalidMessageTypeException, ClassNotFoundException, NoCommonProtocolStackException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        MessageHandler messageHandler = new MessageHandlerImpl(ois, oos);
        messageHandler.writeMessage(protocolHandlerFactory.getSupportedProtocols());
        SupportedProtocols supportedRemotely = messageHandler.getMessage(SupportedProtocols.class);

        SupportedProtocols appliedProtocol = ProtocolDenominator.getCommonDenominator(protocolHandlerFactory.getSupportedProtocols(), supportedRemotely);

        messageHandler.writeMessage(new LoopingProtocolHandler.StartOfLoop());
        messageHandler.writeMessage("hallo");
        messageHandler.writeMessage("welt");
        messageHandler.writeMessage("!");

        messageHandler.writeMessage(new LoopingProtocolHandler.EndOfLoop());
        return true;
    }

    private static ProtocolHandlerFactory buildFactory() {
        return new ProtocolHandlerFactory() {
            @Override
            public ProtocolHandler createHandler(SupportedProtocols commonProtocolStack) {
                return new LoopingProtocolHandler(new ProtocolHandler() {
                    @Override
                    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
                        String message = messageHandler.getMessage(String.class);
                        System.out.println("received message text: " + message);
                    }

                    @Override
                    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
                        return messageHandler.getNextMessageType().equals(String.class.getCanonicalName());
                    }
                });
            }

            @Override
            public SupportedProtocols getSupportedProtocols() {
                return new SupportedProtocols("dummy", "1");
            }
        };

    }

    public static class InvalidMessageTypeException extends Exception {

        public InvalidMessageTypeException() {
        }

        public InvalidMessageTypeException(String message) {
            super(message);
        }
    }
}
