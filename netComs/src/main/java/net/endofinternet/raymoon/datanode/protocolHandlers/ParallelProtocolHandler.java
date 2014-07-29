/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.protocolHandlers;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.datanode.App;
import net.endofinternet.raymoon.datanode.App.InvalidMessageTypeException;
import net.endofinternet.raymoon.datanode.MessageHandler;
import net.endofinternet.raymoon.datanode.ProtocolHandler;
import net.endofinternet.raymoon.datanode.ProtocolHandlerFactory;
import net.endofinternet.raymoon.datanode.SupportedProtocols;

/**
 *
 * @author raymoon
 */
public class ParallelProtocolHandler implements ProtocolHandler {

    public ParallelProtocolHandler(ProtocolHandlerFactory factory, SupportedProtocols protocols) {
        this.factory = factory;
        this.protocols = protocols;
    }

    @Override
    public void handle(MessageHandler messageHandler) throws IOException, App.InvalidMessageTypeException {
        if (messageHandler.getNextMessageType().equals(StartThread.class.getCanonicalName())) {
            handleThreadStart(messageHandler);
        } else if (messageHandler.getNextMessageType().equals(EndThread.class.getCanonicalName())) {
            handleThreadEnd(messageHandler);
        } else if (messageHandler.getNextMessageType().equals(MessageToThread.class.getCanonicalName())) {
            handleThreadMessage(messageHandler);
        } else {
            throw new App.InvalidMessageTypeException("encountered unsupported message type '" + messageHandler.getNextMessageType() + "'");
        }
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, App.InvalidMessageTypeException {

        return messageHandler.getNextMessageType().equals(StartThread.class.getCanonicalName())
                || messageHandler.getNextMessageType().equals(EndThread.class.getCanonicalName())
                || messageHandler.getNextMessageType().equals(MessageToThread.class.getCanonicalName());
    }
    private final ProtocolHandlerFactory factory;
    private final SupportedProtocols protocols;
    private final Map<Integer, ProtocolHandler> handlers = new HashMap<Integer, ProtocolHandler>();
    private final Map<Integer, Thread> processingThread = new HashMap<Integer, Thread>();
    private final Map<Integer, MyMessageHandler> messageHandlers = new HashMap<Integer, MyMessageHandler>();

    private void handleThreadStart(MessageHandler messageHandler) throws InvalidMessageTypeException, IOException {
        StartThread message = messageHandler.getMessage(StartThread.class);

        synchronized (handlers) {
            if (handlers.containsKey(message.id)) {
                throw new InvalidMessageTypeException("there is already a thread with id '" + message.id + "'");
            }

            handlers.put(message.id, factory.createHandler(protocols));
            messageHandlers.put(message.id, buildMessageHandler(messageHandler, message.id));
            processingThread.put(message.id, buildProcessingThread(messageHandlers.get(message.id), handlers.get(message.id)));
            processingThread.get(message.id).start();
        }
    }

    private void handleThreadEnd(MessageHandler messageHandler) throws InvalidMessageTypeException, IOException {
        EndThread message = messageHandler.getMessage(EndThread.class);

        synchronized (handlers) {
            if (!handlers.containsKey(message.id)) {
                throw new InvalidMessageTypeException("there is no thread with id '" + message.id + "'");
            }

            handlers.remove(message.id);
            messageHandlers.remove(message.id).dispose();
            processingThread.remove(message.id);
        }
    }

    private void handleThreadMessage(MessageHandler messageHandler) throws InvalidMessageTypeException, IOException {
        MessageToThread message = messageHandler.getMessage(MessageToThread.class);

        MyMessageHandler handler;
        synchronized (handlers) {
            if (!messageHandlers.containsKey(message.id)) {
                throw new InvalidMessageTypeException("there is no thread with id '" + message.id + "'");
            }

            handler = messageHandlers.get(message.id);
        }

        handler.enqueue(message);
    }

    private MyMessageHandler buildMessageHandler(MessageHandler messageHandler, int threadID) {
        return new MyMessageHandler(messageHandler, threadID);
    }

    private Thread buildProcessingThread(final MessageHandler subHandler, final ProtocolHandler protocolHandler) {
        return new Thread("parallel protocol handler processing thread") {
            @Override
            public void run() {
                try {
                    protocolHandler.handle(subHandler);
                    System.out.println("end of processing thread");
                } catch (IOException ex) {
                    Logger.getLogger(ParallelProtocolHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidMessageTypeException ex) {
                    Logger.getLogger(ParallelProtocolHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    private static class MyMessageHandler implements MessageHandler {

        private final List<MessageToThread> queuedMessages = new LinkedList<>();
        private boolean disposed = false;
        private final MessageHandler messageHandler;
        private final int threadID;

        public MyMessageHandler(MessageHandler messageHandler, int threadID) {
            this.messageHandler = messageHandler;
            this.threadID = threadID;
        }

        @Override
        public <T> T getMessage(Class<T> aClass) throws InvalidMessageTypeException, IOException {
            String messageType;
            String payload;
            synchronized (queuedMessages) {
                if (queuedMessages.isEmpty()) {
                    try {
                        queuedMessages.wait();
                    } catch (InterruptedException ex) {
                        throw new IOException("failed to wait for next message");
                    }
                }

                if (disposed && queuedMessages.isEmpty()) {
                    throw new IOException("thread has been disposed");
                }

                messageType = queuedMessages.get(0).messageType;
                if (!messageType.equals(aClass.getCanonicalName())) {
                    throw new App.InvalidMessageTypeException("expected " + aClass.getCanonicalName() + ", encountered " + messageType);
                }

                payload = queuedMessages.get(0).payload;

                queuedMessages.remove(0);
            }

//            System.out.println("reading: (" + messageType + ") " + payload);

            return new Gson().fromJson(payload, aClass);
        }

        @Override
        public String getNextMessageType() throws IOException {
            synchronized (queuedMessages) {
                if (queuedMessages.isEmpty()) {
                    try {
                        queuedMessages.wait();
                    } catch (InterruptedException ex) {
                        throw new IOException("failed to wait for next message");
                    }
                }

                if (disposed && queuedMessages.isEmpty()) {
                    throw new IOException("thread has been disposed");
                }

                return queuedMessages.get(0).messageType;
            }
        }

        @Override
        public void writeMessage(Object message) throws IOException {
            MessageFromThread fromThread = new MessageFromThread();
            fromThread.messageType = message.getClass().getCanonicalName();
            fromThread.payload = new Gson().toJson(message);
            fromThread.id = threadID;

            messageHandler.writeMessage(fromThread);
        }

        public void dispose() {
            disposed = true;

            synchronized (queuedMessages) {
                queuedMessages.notify();
            }
        }

        public void enqueue(MessageToThread message) throws InvalidMessageTypeException {
            if ( disposed ){
                throw new InvalidMessageTypeException("thread with id '"+threadID+"' is already disposed, cannot enqueue further messages");
            }
            synchronized (queuedMessages) {
                queuedMessages.add(message);
                queuedMessages.notify();
            }
        }
    }

    public static class StartThread {

        int id;

        public StartThread(int id) {
            this.id = id;
        }
    }

    public static class EndThread {

        int id;

        public EndThread(int id) {
            this.id = id;
        }
    }

    public static class MessageToThread {

        int id;
        String messageType;
        String payload;

        public MessageToThread(int id, Object object) {
            this.id = id;

            this.messageType = object.getClass().getCanonicalName();
            this.payload = new Gson().toJson(object);
        }
    }

    public static class MessageFromThread {

        int id;
        String messageType;
        String payload;
    }
}
