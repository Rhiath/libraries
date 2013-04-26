/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib;

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
