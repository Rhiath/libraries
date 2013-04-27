/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components.lifecycle.exceptions;

import de.raytec.java.lib.ExceptionBase;

/**
 *
 * @author raymoon
 */
public class InitializationFailedException extends ExceptionBase {

    public InitializationFailedException(String message) {
        super(message);
    }

    public InitializationFailedException(String message, Exception cause) {
        super(message, cause);
    }
    
}
