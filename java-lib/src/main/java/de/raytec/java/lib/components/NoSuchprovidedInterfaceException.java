/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components;

import de.raytec.java.lib.ExceptionBase;

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
