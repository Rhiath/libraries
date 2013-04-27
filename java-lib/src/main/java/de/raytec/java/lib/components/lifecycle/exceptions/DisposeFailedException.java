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
public class DisposeFailedException extends ExceptionBase {

    public DisposeFailedException(String message) {
        super(message);
    }

    public DisposeFailedException(String message, Exception cause) {
        super(message, cause);
    }
    
}
