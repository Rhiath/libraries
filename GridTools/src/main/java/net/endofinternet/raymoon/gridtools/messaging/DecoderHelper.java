/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools.messaging;

import net.endofinternet.raymoon.gridtools.Packet;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;

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
