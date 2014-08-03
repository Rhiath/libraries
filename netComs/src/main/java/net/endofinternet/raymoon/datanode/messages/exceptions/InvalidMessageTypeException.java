/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.messages.exceptions;

/**
 *
 * @author raymoon
 */
public class InvalidMessageTypeException extends Exception {

    public InvalidMessageTypeException() {
    }

    public InvalidMessageTypeException(String message) {
        super(message);
    }

    public InvalidMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
