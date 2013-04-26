/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.transmission;

/**
 *
 * @author raymoon
 */
public interface TransmissionObserver {
    
    /**
     * indicates that a certain amount of bytes have been transmitted from the
     * source to the destination
     * 
     * @param byteCount 
     */
    public void dataTransmitted(long byteCount);
    
    /**
     * indicates that not more data will be transmitted from the source to the
     * destination. An invocation of this operation does not indicate if an 
     * error occurred or not.
     */
    public void transmissionFinished();
}
