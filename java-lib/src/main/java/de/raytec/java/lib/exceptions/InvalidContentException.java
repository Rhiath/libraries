/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.exceptions;

import de.raytec.java.lib.ExceptionBase;

/**
 *
 * @author raymoon
 */
public class InvalidContentException extends ExceptionBase {

    public InvalidContentException(String message) {
        super(message);
    }

    public InvalidContentException(String message, Exception cause) {
        super(message, cause);
    }
}
