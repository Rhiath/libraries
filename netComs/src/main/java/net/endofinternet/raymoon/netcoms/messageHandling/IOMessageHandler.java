/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.messageHandling;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author raymoon
 */
public class IOMessageHandler implements MessageHandler {

    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    private final Dictionary dictionary;

    public IOMessageHandler(ObjectInputStream ois, ObjectOutputStream oos, Dictionary dictionary) {
        this.ois = ois;
        this.oos = oos;
        this.dictionary = dictionary;
    }

    @Override
    public void send(Object message) throws IOException {
        int typeCode = dictionary.getTypeCode(message.getClass());

        if (typeCode == -1) {
            throw new IOException("message cannot be transmitted, class '" + message.getClass() + "' is not listed in the dictionary");
        }

        oos.writeInt(typeCode);
        writeMessage(new Gson().toJson(message).getBytes());
    }

    @Override
    public Object receive() throws IOException {
        Class classType = dictionary.getClass(ois.readInt());

        return new Gson().fromJson(new String(readMessage()), classType);
    }

    private void writeMessage(byte[] data) throws IOException {
        oos.writeInt(data.length);
        oos.write(data);
    }

    private byte[] readMessage() throws IOException {
        int bytesToRead = ois.readInt();
        byte[] target = new byte[bytesToRead];

        ois.readFully(target);

        return target;
    }
    
    public void flush() throws IOException {
        oos.flush();
    }
}
