/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces.exceptions;

/**
 *
 * @author raymoon
 */
public class CommandExecutionFailedException extends Exception {

    public CommandExecutionFailedException(String message) {
        super(message);
    }

    public CommandExecutionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
