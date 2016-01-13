/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms.messageHandling;

import java.io.IOException;

/**
 *
 * @author raymoon
 */
public interface MessageHandler {

    public void send(Object message) throws IOException;

    public Object receive() throws IOException;
}
