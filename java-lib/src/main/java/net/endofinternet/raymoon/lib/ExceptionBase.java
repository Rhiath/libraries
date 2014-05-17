/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib;

/**
 *
 * @author raymoon
 */
public abstract class ExceptionBase extends Exception {

    public ExceptionBase(String message) {
        super(message);
    }

    public ExceptionBase(String message, Exception cause) {
        super(message, cause);
    }
    
}
