/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.validation;

import java.util.List;

/**
 * validates data and reports the validation result
 * 
 * @author raymoon
 */
public interface Validator<T> {
    public List<ValidationResult> validate(T validationtarget) throws ValidationFailedException;
}
