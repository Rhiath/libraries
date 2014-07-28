/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

import java.io.IOException;
import net.endofinternet.raymoon.datanode.App.InvalidMessageTypeException;

/**
 *
 * @author Ray
 */
public interface ProtocolHandler {

    public void handle(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException;

    public boolean responsibleForNextMessage(MessageHandler messageHandler) throws IOException, InvalidMessageTypeException;
}
