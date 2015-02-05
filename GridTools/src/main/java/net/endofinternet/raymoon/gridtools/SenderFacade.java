/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author raymoon
 */
class SenderFacade {

    static void sendStoreAck(Sender sender, byte[] responseAddress, short reponsePort, int blockID) {
        byte[] dataBlock = new byte[1+1+4];
        ByteBuffer buffer = ByteBuffer.wrap(dataBlock);
        buffer.order(ByteOrder.BIG_ENDIAN); // network byte order
        buffer.put((byte) 'A'); // ack
        buffer.put((byte) 'S'); // store
        buffer.putInt(blockID); // blockID
        sender.sendAsynch(dataBlock, responseAddress, reponsePort);
    }
    
}
