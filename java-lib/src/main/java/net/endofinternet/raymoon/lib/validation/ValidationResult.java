/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.validation;

/**
 * result of validation. The subclasses indicate the actual type of validation result.
 * Validation results can range from informative observations over fishy content
 * to violations (errors / bad data)
 * 
 * @author raymoon
 */
public abstract class ValidationResult {
    public abstract String getDescription();
}
