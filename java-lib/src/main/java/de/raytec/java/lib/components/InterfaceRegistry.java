/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author raymoon
 */
public class InterfaceRegistry implements InterfaceProvider{

    private final Map<Class, Object> providedInterfaces = new HashMap<Class, Object>();

    public <T> T getProvidedInterface(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        if (!providedInterfaces.containsKey(interfaceClass)) {
            throw new NoSuchprovidedInterfaceException("the interface '" + interfaceClass.getSimpleName() + "' is not provided by this component");
        }

        return (T) providedInterfaces.get(interfaceClass);
    }

    public <T> void provideInterface(T object, Class<T> providedClass) {
        providedInterfaces.put(providedClass, object);
    }
}
