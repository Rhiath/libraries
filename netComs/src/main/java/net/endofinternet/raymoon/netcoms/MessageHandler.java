/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms;

import java.io.IOException;
import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;

/**
 *
 * @author raymoon
 */
public interface MessageHandler {

    /**
     * retrieves the next message object
     *
     * @param <T> the type of the object
     * @param aClass the class of the object type to be read
     * @return an object of the required type (no subclasses)
     * @throws InvalidMessageTypeException if the read object differs in class
     * @throws IOException when reading the required data failed
     */
    <T> T getMessage(Class<T> aClass) throws InvalidMessageTypeException, IOException;

    /**
     * reads the next message object in binary form (e.g. for decompression)
     *
     * @return the byte[] of the next object
     * @throws IOException when reading the required data failed
     */
    byte[] getRawMessage() throws IOException;

    /**
     * provides the consumer of the handler with the ability to identify the
     * next message type before it is read. This is useful if there is a choice
     * between possible messages. The next message type be read any number
     * before the actual message object is read.
     *
     * @return the string representation of the message type
     * (Class.getCanonicalName)
     * @throws IOException when reading the required data failed
     */
    String getNextMessageType() throws IOException;

    /**
     * writes message object. The object is automatically translated into a
     * Jason string. The type of the message will be determined from the
     * message's class object (Class.getCanonicalName).
     *
     * @param message the message to transmit
     * @throws IOException when writing the required data failed
     */
    void writeMessage(Object message) throws IOException;

    /**
     * writes plain byte array. This can be used to transfer compressed messages
     * or other binary data.
     *
     * @param type The type to use for transmission
     * @param message the binary payload to transmit
     * @throws IOException when writing the required data failed
     */
    void writeMessage(String type, byte[] message) throws IOException;
}
