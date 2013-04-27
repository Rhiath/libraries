/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components;

/**
 *
 * @author raymoon
 */
public interface InterfaceProvider {
    public <T> T getProvidedInterface(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException;
}
