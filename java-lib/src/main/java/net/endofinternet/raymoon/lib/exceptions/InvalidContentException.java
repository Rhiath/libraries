/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.exceptions;

/**
 *
 * @author raymoon
 */
public class InvalidContentException extends RuntimeException {

    public InvalidContentException(String message) {
        super(message);
    }

    public InvalidContentException(String message, Exception cause) {
        super(message, cause);
    }
}
