/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.validation;

import de.raytec.java.lib.ExceptionBase;

/**
 * to be thrown during the validation if performing the validation failed. It does
 * not indicate if the validated data is valid or not
 * 
 * @author raymoon
 */
public class ValidationFailedException extends ExceptionBase {

    public ValidationFailedException(String message) {
        super(message);
    }

    public ValidationFailedException(String message, Exception cause) {
        super(message, cause);
    }
    
}
