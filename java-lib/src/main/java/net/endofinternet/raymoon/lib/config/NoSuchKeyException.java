/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.config;

import net.endofinternet.raymoon.lib.ExceptionBase;

/**
 *
 * @author raymoon
 */
public class NoSuchKeyException extends ExceptionBase {

    public NoSuchKeyException(String message) {
        super(message);
    }

    public NoSuchKeyException(String message, Exception cause) {
        super(message, cause);
    }
    
}
