/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.components.lifecycle.exceptions;

import net.endofinternet.raymoon.lib.ExceptionBase;

/**
 *
 * @author raymoon
 */
public class StartFailedException extends ExceptionBase {

    public StartFailedException(String message) {
        super(message);
    }

    public StartFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
