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
public class PauseFailedException extends ExceptionBase {

    public PauseFailedException(String message) {
        super(message);
    }

    public PauseFailedException(String message, Exception cause) {
        super(message, cause);
    }
    
}
