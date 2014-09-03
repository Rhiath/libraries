/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.endofinternet.raymoon.netcoms.App;
import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;
import net.endofinternet.raymoon.netcoms.MessageHandler;

/**
 *
 * @author raymoon
 */
public class CompressingMessageHandler extends AbstractMessageHandler {

    private final MessageHandler handler;

    public CompressingMessageHandler(MessageHandler handler) {
        this.handler = handler;
    }

    @Override
    public byte[] getRawMessage() throws IOException {
        return decompress(handler.getRawMessage());
    }

    @Override
    public String getNextMessageType() throws IOException {
        return handler.getNextMessageType();
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
