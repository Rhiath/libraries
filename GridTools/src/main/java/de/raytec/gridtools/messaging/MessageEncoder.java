/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools.messaging;

import de.raytec.java.lib.exceptions.InvalidContentException;

/**
 *
 * @author raymoon
 */
public interface MessageEncoder<T extends Message> {
    public byte[] encode(T message) throws InvalidContentException;
}
