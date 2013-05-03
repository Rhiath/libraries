/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools.messaging;

import de.raytec.java.lib.exceptions.InvalidContentException;
import java.nio.ByteBuffer;

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
