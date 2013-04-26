/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import de.raytec.java.lib.ExceptionBase;

/**
 *
 * @author raymoon
 */
public class InvalidValueContentException extends ExceptionBase {

    public InvalidValueContentException(String message) {
        super(message);
    }

    public InvalidValueContentException(String message, Exception cause) {
        super(message, cause);
    }
}
