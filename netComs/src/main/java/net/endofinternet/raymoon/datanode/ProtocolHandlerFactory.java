/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

import net.endofinternet.raymoon.datanode.messages.SupportedProtocolMessage;

/**
 *
 * @author Ray
 */
public interface ProtocolHandlerFactory {

    public ProtocolHandler createHandler(SupportedProtocolMessage commonProtocolStack);

    public SupportedProtocolMessage getSupportedProtocols();

}
