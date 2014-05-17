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
public class InvalidValueContentException extends ExceptionBase {

    public InvalidValueContentException(String message) {
        super(message);
    }

    public InvalidValueContentException(String message, Exception cause) {
        super(message, cause);
    }
}
