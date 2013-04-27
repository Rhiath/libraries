/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components;


/**
 *
 * @author raymoon
 */
public interface Component extends InterfaceProvider {
    public void prepareNewConfig();
    public void applyNewConfig();
    
}
