/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.protocolHandlers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.datanode.App;
import net.endofinternet.raymoon.datanode.MessageHandler;
import net.endofinternet.raymoon.datanode.ProtocolHandler;

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
    public void handle(MessageHandler messageHandler) throws IOException, App.InvalidMessageTypeException {
        for ( ProtocolHandler handler : handlers){
            handler.handle(messageHandler);
        }
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, App.InvalidMessageTypeException {
        return handlers.get(0).responsibleForNextMessage(messageHandler);
    }
}
