/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.protocolHandlers;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.endofinternet.raymoon.datanode.App;
import net.endofinternet.raymoon.datanode.MessageHandler;

/**
 *
 * @author raymoon
 */
public class CompressingMessageHandler implements MessageHandler {

    private final MessageHandler handler;

    public CompressingMessageHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public <T> T getMessage(Class<T> aClass) throws App.InvalidMessageTypeException, IOException {
        String messageType = getNextMessageType();

        if (!messageType.equals(aClass.getCanonicalName())) {
            throw new App.InvalidMessageTypeException("expected " + aClass.getCanonicalName() + ", encountered " + messageType);
        }

        final String payload = new String(getRawMessage());

//        System.out.println("reading: (" + messageType + ") " + payload);

        return new Gson().fromJson(payload, aClass);
    }

    @Override
    public byte[] getRawMessage() throws App.InvalidMessageTypeException, IOException {
        return decompress(handler.getRawMessage());
    }

    @Override
    public String getNextMessageType() throws IOException {
        return handler.getNextMessageType();
    }

    @Override
    public void writeMessage(Object message) throws IOException {
        writeMessage(message.getClass().getCanonicalName(), new Gson().toJson(message).getBytes());
    }

    @Override
    public void writeMessage(String type, byte[] message) throws IOException {
        handler.writeMessage(type, compress(message));
    }

    private byte[] compress(byte[] message) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeInt(message.length);
        oos.flush();
        try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
            gos.write(message);
        }
        final byte[] retValue = bos.toByteArray();

//        float ratio = retValue.length;
//        ratio /= (float) message.length;
//        ratio *= 100.0f;
//        System.out.println("COMPRESS: " + message.length + " --> " + retValue.length + " (" + (int) ratio + "%)");

        return retValue;

    }

    private byte[] decompress(byte[] rawMessage) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(rawMessage);
        ObjectInputStream ois = new ObjectInputStream(bis);

        int rawLength = ois.readInt();
        byte[] data = new byte[rawLength];

        GZIPInputStream gis = new GZIPInputStream(bis);
        int readCount = 0;
        int read = 0;
        while (readCount != rawLength) {
            read = gis.read(data);
            if (read == -1) {
                throw new IOException("failed to read data from input stream");
            }
            readCount += read;
        }

//        float ratio = data.length;
//        ratio /= (float) rawMessage.length;
//        ratio *= 100.0f;
//        System.out.println("DECOMPRESS: " + rawMessage.length + " --> " + data.length + " (" + (int) ratio + "%)");


        return data;
    }
}
