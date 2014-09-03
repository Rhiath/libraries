/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.protocolHandlers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.netcoms.App;
import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;
import net.endofinternet.raymoon.netcoms.MessageHandler;
import net.endofinternet.raymoon.netcoms.ProtocolHandler;

/**
 *
 * @author raymoon
 */
public class SequenceProtocolHandler implements ProtocolHandler {
    
    private final List<ProtocolHandler> handlers;

    public SequenceProtocolHandler(List<ProtocolHandler> handlers) {
        this.handlers = new LinkedList<>(handlers);
    }

    public SequenceProtocolHandler(ProtocolHandler... handlers) {
        this.handlers = new LinkedList<>();
        for (ProtocolHandler handler : handlers) {
            this.handlers.add(handler);
        }
    }

    @Override
    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        for ( ProtocolHandler handler : handlers){
            handler.handle(messageHandler);
        }
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        return handlers.get(0).responsibleForNextMessage(messageHandler);
    }
}
