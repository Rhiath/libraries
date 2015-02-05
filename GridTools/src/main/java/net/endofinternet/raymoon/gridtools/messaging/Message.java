/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools.messaging;

/**
 *
 * @author raymoon
 */
public interface Message <T extends Message>{
   
    public MessageEncoder<T> getEncoder();
}
