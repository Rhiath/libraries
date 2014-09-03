/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.protocolHandlers;

import net.endofinternet.raymoon.netcoms.messages.CompressingMessageHandler;
import java.io.IOException;
import net.endofinternet.raymoon.netcoms.App;
import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;
import net.endofinternet.raymoon.netcoms.MessageHandler;
import net.endofinternet.raymoon.netcoms.ProtocolHandler;

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
    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        handler.handle(adapt(messageHandler));
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        return handler.responsibleForNextMessage(adapt(messageHandler));
    }

    private MessageHandler adapt(MessageHandler messageHandler) {
        return new CompressingMessageHandler(messageHandler);
    }
}
