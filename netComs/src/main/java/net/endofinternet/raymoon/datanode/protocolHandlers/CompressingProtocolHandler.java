/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.protocolHandlers;

import net.endofinternet.raymoon.datanode.messages.CompressingMessageHandler;
import java.io.IOException;
import net.endofinternet.raymoon.datanode.App;
import net.endofinternet.raymoon.datanode.MessageHandler;
import net.endofinternet.raymoon.datanode.ProtocolHandler;

/**
 *
 * @author raymoon
 */
public class CompressingProtocolHandler implements ProtocolHandler {

    private final ProtocolHandler handler;

    public CompressingProtocolHandler(ProtocolHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(MessageHandler messageHandler) throws IOException, App.InvalidMessageTypeException {
        handler.handle(adapt(messageHandler));
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, App.InvalidMessageTypeException {
        return handler.responsibleForNextMessage(adapt(messageHandler));
    }

    private MessageHandler adapt(MessageHandler messageHandler) {
        return new CompressingMessageHandler(messageHandler);
    }
}
