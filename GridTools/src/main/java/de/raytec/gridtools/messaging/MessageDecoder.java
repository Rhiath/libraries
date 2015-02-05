/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools.messaging;

import java.nio.ByteBuffer;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;

/**
 *
 * @author raymoon
 */
public interface MessageDecoder<T extends Message> {
    public int getMessageSize();
    public byte getOpCode();
    public byte getVersion();
    public T decode(ByteBuffer buffer) throws InvalidContentException;
}
