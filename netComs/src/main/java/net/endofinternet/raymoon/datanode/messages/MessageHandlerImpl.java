/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.endofinternet.raymoon.datanode.App;

/**
 *
 * @author raymoon
 */
public class MessageHandlerImpl extends AbstractMessageHandler {

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private String nextMessageType;
    private boolean nextMessageTypeRead = false;

    public MessageHandlerImpl(ObjectInputStream ois, ObjectOutputStream oos) {
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public synchronized byte[] getRawMessage() throws App.InvalidMessageTypeException, IOException {
        String messageType = getNextMessageType();
        nextMessageTypeRead = false;
        return getNextBytes();
    }

    @Override
    public synchronized String getNextMessageType() throws IOException {
        if (!nextMessageTypeRead) {
            nextMessageType = getNextString();
            nextMessageTypeRead = true;
        }

        return nextMessageType;
    }

    private byte[] getNextBytes() throws IOException {
        int length = ois.readInt();
        byte[] data = new byte[length];
        ois.readFully(data);
        return data;
    }

    private String getNextString() throws IOException {
        return new String(getNextBytes());
    }

    @Override
    public synchronized  void writeMessage(String type, byte[] payload) throws IOException {

//        System.out.println("writing: (" + message.getClass().getCanonicalName() + ") " + payload);
        oos.writeInt(type.length());
        oos.write(type.getBytes());
        oos.writeInt(payload.length);
        oos.write(payload);
        oos.flush();    }
}
