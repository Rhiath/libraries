/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.messages;

import net.endofinternet.raymoon.datanode.MessageHandler;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.endofinternet.raymoon.datanode.App;

/**
 *
 * @author raymoon
 */
public class MessageHandlerImpl implements MessageHandler {

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private String nextMessageType;
    private boolean nextMessageTypeRead = false;

    public MessageHandlerImpl(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public synchronized <T> T getMessage(Class<T> aClass) throws App.InvalidMessageTypeException, IOException {
        String messageType = getNextMessageType();

        if (!messageType.equals(aClass.getCanonicalName())) {
            throw new App.InvalidMessageTypeException("expected " + aClass.getCanonicalName() + ", encountered " + messageType);
        }

        nextMessageTypeRead = false;
        final String payload = getNextString();

//        System.out.println("reading: (" + messageType + ") " + payload);

        return new Gson().fromJson(payload, aClass);

    }

    @Override
    public synchronized void writeMessage(Object message) throws IOException {
        String payload = new Gson().toJson(message);
//        System.out.println("writing: (" + message.getClass().getCanonicalName() + ") " + payload);
        oos.writeInt(message.getClass().getCanonicalName().length());
        oos.write(message.getClass().getCanonicalName().getBytes());
        oos.writeInt(payload.length());
        oos.write(payload.getBytes());
        oos.flush();
    }

    @Override
    public synchronized String getNextMessageType() throws IOException {
        if (!nextMessageTypeRead) {
            nextMessageType = getNextString();
            nextMessageTypeRead = true;
        }

        return nextMessageType;
    }

    private String getNextString() throws IOException {
        int length = ois.readInt();
        byte[] data = new byte[length];
        ois.readFully(data);
        return new String(data);
    }
}
