package net.endofinternet.raymoon.netcoms;

import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;
import com.barchart.udt.net.NetServerSocketUDT;
import com.barchart.udt.net.NetSocketUDT;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.netcoms.messages.AbstractMessageHandler;
import net.endofinternet.raymoon.netcoms.messages.IOMessageHandler;
import net.endofinternet.raymoon.netcoms.messages.exceptions.NoCommonProtocolStackException;
import net.endofinternet.raymoon.netcoms.messages.CompressingMessageHandler;
import net.endofinternet.raymoon.netcoms.protocolHandlers.CompressingProtocolHandler;
import net.endofinternet.raymoon.netcoms.protocolHandlers.LoopingProtocolHandler;
import net.endofinternet.raymoon.netcoms.protocolHandlers.ParallelProtocolHandler;
import net.endofinternet.raymoon.netcoms.protocolHandlers.invocation.InvocationHandler;
import net.endofinternet.raymoon.netcoms.protocolHandlers.invocation.ProxyBuilder;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        startServiceProvider(9000);
        startServiceConsumer(9001, new InetSocketAddress("localhost", 9000));
        
//        
//        final ServerSocket ss = new ServerSocket(9000);
//        final Socket cs = new Socket("localhost", 9000);
        
       
        
//        final PipedInputStream pis1 = new PipedInputStream(10240);
//        final PipedInputStream pis2 = new PipedInputStream(10240);
//        
//        final PipedOutputStream pos1 = new PipedOutputStream(pis1);
//        final PipedOutputStream pos2 = new PipedOutputStream(pis2);
//        
//        new Thread(){
//
//            @Override
//            public void run() {
//                final ProtocolHandlerFactory factory = buildFactory();
//                try {
//                    Socket s = ss.accept();
//                    serviceTask(s.getInputStream(), s.getOutputStream(), factory);
//                } catch (IOException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (InvalidMessageTypeException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NoCommonProtocolStackException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }.start();
//        
//        
//        new Thread(){
//
//            @Override
//            public void run() {
//                final ProtocolHandlerFactory factory = buildFactory();
//                try {
//                    clientTask(cs.getInputStream(), cs.getOutputStream(), factory);
//                } catch (IOException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (InvalidMessageTypeException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (ClassNotFoundException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (NoCommonProtocolStackException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (Exception ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }.start();
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
            } catch (Exception ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean serviceTask(InputStream is, OutputStream os, ProtocolHandlerFactory protocolHandlerFactory) throws IOException, InvalidMessageTypeException, ClassNotFoundException, NoCommonProtocolStackException {
        is = new BufferedInputStream(is);
//        os = new BufferedOutputStream(os);
        
        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        MessageHandler messageHandler = new IOMessageHandler(ois, oos);
        messageHandler.writeMessage(protocolHandlerFactory.getSupportedProtocols());
        SupportedProtocols supportedRemotely = messageHandler.getMessage(SupportedProtocols.class);

        SupportedProtocols appliedProtocol = ProtocolDenominator.getCommonDenominator(protocolHandlerFactory.getSupportedProtocols(), supportedRemotely);

        messageHandler = new CompressingMessageHandler(messageHandler);

        protocolHandlerFactory.createHandler(appliedProtocol).handle(messageHandler);
        System.out.println("end of service task");

        return true;
    }

    public static boolean clientTask(InputStream is, OutputStream os, ProtocolHandlerFactory protocolHandlerFactory) throws IOException, InvalidMessageTypeException, ClassNotFoundException, NoCommonProtocolStackException, Exception {
        is = new BufferedInputStream(is);
        os = new BufferedOutputStream(os);
        
        ObjectOutputStream oos = new ObjectOutputStream(os);
        ObjectInputStream ois = new ObjectInputStream(is);

        MessageHandler messageHandler = new IOMessageHandler(ois, oos);
        messageHandler.writeMessage(protocolHandlerFactory.getSupportedProtocols());
        SupportedProtocols supportedRemotely = messageHandler.getMessage(SupportedProtocols.class);

        SupportedProtocols appliedProtocol = ProtocolDenominator.getCommonDenominator(protocolHandlerFactory.getSupportedProtocols(), supportedRemotely);

        messageHandler = new CompressingMessageHandler(messageHandler); // start compression

        messageHandler.writeMessage(new LoopingProtocolHandler.StartOfLoop());

        TestInterface instance = ProxyBuilder.invoke(TestInterface.class, messageHandler);

        for (int i = 0; i < 10000; i++) {
            log(i);
//            instance.anotherMethod();
            instance.halloWelt();
        }

        messageHandler.writeMessage(new LoopingProtocolHandler.EndOfLoop());

        return true;
    }

    private static void log(int i) {
        if (i % 100 == 0) {
            System.out.println(System.currentTimeMillis()+"  invoked " + i + " times");
        }
    }

    public static interface TestInterface {

        public int halloWelt() throws Exception;

        public void anotherMethod();
    }

    private static ProtocolHandlerFactory buildFactory() {
        return new ProtocolHandlerFactory() {
            @Override
            public ProtocolHandler createHandler(final SupportedProtocols commonProtocolStack) {
                InvocationHandler handler = new InvocationHandler();

                handler.register(TestInterface.class, new MyTestImpl());

                return new LoopingProtocolHandler(handler);
            }

            @Override
            public SupportedProtocols getSupportedProtocols() {
                return new SupportedProtocols("dummy", "1");
            }
        };

    }
}
