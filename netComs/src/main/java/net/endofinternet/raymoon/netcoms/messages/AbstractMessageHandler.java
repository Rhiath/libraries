/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.messages;

import com.google.gson.Gson;
import java.io.IOException;
import net.endofinternet.raymoon.netcoms.App;
import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;
import net.endofinternet.raymoon.netcoms.MessageHandler;

/**
 *
 * @author raymoon
 */
public abstract class AbstractMessageHandler implements MessageHandler {

    @Override
    public <T> T getMessage(Class<T> aClass) throws InvalidMessageTypeException, IOException {
        String messageType = getNextMessageType();
        if (!messageType.equals(aClass.getCanonicalName())) {
            throw new InvalidMessageTypeException("expected " + aClass.getCanonicalName() + ", encountered " + messageType);
        }
        final String payload = new String(getRawMessage());
        return new Gson().fromJson(payload, aClass);
    }

    @Override
    public void writeMessage(Object message) throws IOException {
        writeMessage(message.getClass().getCanonicalName(), new Gson().toJson(message).getBytes());
    }
    
}
