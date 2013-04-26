/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.transmission;

import de.raytec.java.lib.ExceptionBase;

/**
 *  used whenever there was an error during the transmission
 * 
 * @author raymoon
 */
public class TransmissionException extends ExceptionBase {

    public TransmissionException(String message) {
        super(message);
    }

    public TransmissionException(String message, Exception cause) {
        super(message, cause);
    }  
}
