/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.components;

import net.endofinternet.raymoon.lib.ExceptionBase;

/**
 *
 * @author raymoon
 */
public class NoSuchprovidedInterfaceException extends ExceptionBase {

    public NoSuchprovidedInterfaceException(String message) {
        super(message);
    }

    public NoSuchprovidedInterfaceException(String message, Exception cause) {
        super(message, cause);
    }
    
}
