/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.messages;

import com.google.gson.Gson;
import java.io.IOException;
import net.endofinternet.raymoon.datanode.App;
import net.endofinternet.raymoon.datanode.MessageHandler;

/**
 *
 * @author raymoon
 */
public abstract class AbstractMessageHandler implements MessageHandler {

    @Override
    public <T> T getMessage(Class<T> aClass) throws App.InvalidMessageTypeException, IOException {
        String messageType = getNextMessageType();
        if (!messageType.equals(aClass.getCanonicalName())) {
            throw new App.InvalidMessageTypeException("expected " + aClass.getCanonicalName() + ", encountered " + messageType);
        }
        final String payload = new String(getRawMessage());
        return new Gson().fromJson(payload, aClass);
    }

    @Override
    public void writeMessage(Object message) throws IOException {
        writeMessage(message.getClass().getCanonicalName(), new Gson().toJson(message).getBytes());
    }
    
}
