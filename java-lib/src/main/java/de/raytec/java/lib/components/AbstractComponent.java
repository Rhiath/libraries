/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components;


/**
 *
 * @author raymoon
 */
public abstract class AbstractComponent implements Component {

    private final InterfaceRegistry externalRegistry = new InterfaceRegistry();
    private final InterfaceRegistry internalRegistry = new InterfaceRegistry();
    private final InterfaceProvider parentProvider;

    public AbstractComponent(InterfaceProvider parentProvider) {
        this.parentProvider = parentProvider;
    }

    public <T> T getProvidedInterface(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        return externalRegistry.getProvidedInterface(interfaceClass);
    }

    protected <T> void provideInterfaceExternally(T object, Class<T> providedClass) {
        externalRegistry.provideInterface(object, providedClass);
    }

    protected <T> void provideInterfaceInternally(T object, Class<T> providedClass) {
        internalRegistry.provideInterface(object, providedClass);
    }

    protected <T> void provideParentProvidedInterfaceToChildren(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        this.internalRegistry.provideInterface(parentProvider.getProvidedInterface(interfaceClass), interfaceClass);
    }
    
    protected <T> T aquireParentProvidedInterface(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        return parentProvider.getProvidedInterface(interfaceClass);
    }
}
