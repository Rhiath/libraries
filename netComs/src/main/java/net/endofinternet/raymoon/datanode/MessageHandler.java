/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

import java.io.IOException;

/**
 *
 * @author raymoon
 */
public interface MessageHandler {

    <T> T getMessage(Class<T> aClass) throws App.InvalidMessageTypeException, IOException;

    String getNextMessageType() throws IOException;

    void writeMessage(Object message) throws IOException;
    
}
