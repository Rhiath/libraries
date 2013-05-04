/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools.messaging;

import de.raytec.gridtools.Packet;
import de.raytec.java.lib.exceptions.InvalidContentException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author raymoon
 */
public class DecoderHelper {

    public static boolean decoderMatchesPacket( MessageDecoder decoder, Packet packet) {
        byte version = packet.getData()[0];
        byte opcode = packet.getData()[1];
        int length = packet.getLength() - 2;

        return (decoder.getMessageSize() == length) && (decoder.getOpCode() == opcode) && (decoder.getVersion() == version);
    }

    public static <T extends Message> T decodeMessage(byte[] message, MessageDecoder<T> decoder) throws InvalidContentException {
        ByteBuffer buffer = ByteBuffer.wrap(message, 2, message.length - 2);
        buffer.order(ByteOrder.BIG_ENDIAN); // network byte order

        return decoder.decode(buffer);
    }
}
