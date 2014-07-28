/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

/**
 *
 * @author Ray
 */
public interface ProtocolHandlerFactory {

    public ProtocolHandler createHandler(SupportedProtocols commonProtocolStack);

    public SupportedProtocols getSupportedProtocols();

}
