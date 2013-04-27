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
public class InvalidStateTransitionException extends ExceptionBase {

    public InvalidStateTransitionException(String message) {
        super(message);
    }

    public InvalidStateTransitionException(String message, Exception cause) {
        super(message, cause);
    }
}
