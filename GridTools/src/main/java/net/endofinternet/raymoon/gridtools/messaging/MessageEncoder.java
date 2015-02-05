/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools.messaging;

import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;


/**
 *
 * @author raymoon
 */
public interface MessageEncoder<T extends Message> {
    public byte[] encode(T message) throws InvalidContentException;
}
