/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.protocolHandlers;

import java.io.IOException;
import net.endofinternet.raymoon.netcoms.App;
import net.endofinternet.raymoon.netcoms.messages.exceptions.InvalidMessageTypeException;
import net.endofinternet.raymoon.netcoms.MessageHandler;
import net.endofinternet.raymoon.netcoms.ProtocolHandler;

/**
 *
 * @author raymoon
 */
public class LoopingProtocolHandler implements ProtocolHandler {

    private final ProtocolHandler handler;

    public LoopingProtocolHandler(ProtocolHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        messageHandler.getMessage(StartOfLoop.class);

        while (nextMessageIsNotEndOfLoop(messageHandler)) {
            handler.handle(messageHandler);
        }

        messageHandler.getMessage(EndOfLoop.class);
    }

    @Override
    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException {
        return messageHandler.getNextMessageType().equals(StartOfLoop.class.getCanonicalName());
    }

    private boolean nextMessageIsNotEndOfLoop(MessageHandler messageHandler) throws IOException {
        return !messageHandler.getNextMessageType().equals(EndOfLoop.class.getCanonicalName());
    }

    public static class StartOfLoop {
    }

    public static class EndOfLoop {
    }
}
