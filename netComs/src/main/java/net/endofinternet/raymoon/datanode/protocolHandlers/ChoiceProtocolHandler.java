/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.protocolHandlers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.datanode.App;
import net.endofinternet.raymoon.datanode.messages.exceptions.InvalidMessageTypeException;
import net.endofinternet.raymoon.datanode.MessageHandler;
import net.endofinternet.raymoon.datanode.ProtocolHandler;

/**
 *
 * @author raymoon
 */
public class ChoiceProtocolHandler implements ProtocolHandler {

    private final List<ProtocolHandler> handlers;

    public ChoiceProtocolHandler(List<ProtocolHandler> handlers) {
        this.handlers = new LinkedList<>(handlers);
    }

    public ChoiceProtocolHandler(ProtocolHandler... handlers) {
        this.handlers = new LinkedList<>();
        for (ProtocolHandler handler : handlers) {
            this.handlers.add(handler);
        }
    }

    @Override
    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        boolean handled = false;
        for (ProtocolHandler handler : handlers) {
            if (handler.responsibleForNextMessage(messageHandler)) {
                handled = true;
                handler.handle(messageHandler);
            }
        }

        if (!handled) {
            throw new InvalidMessageTypeException("no handler found responsible for message type '" + messageHandler.getNextMessageType() + "'");
        }
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        for (ProtocolHandler handler : handlers) {
            if (handler.responsibleForNextMessage(messageHandler)) {
                return true;
            }
        }

        return false;
    }
}
