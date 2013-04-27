/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components;

import de.raytec.java.lib.components.lifecycle.AbstractLifeCycleManaged;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
public abstract class AbstractComponent extends AbstractLifeCycleManaged implements Component {

    private final InterfaceRegistry externalRegistry = new InterfaceRegistry();
    private final InterfaceRegistry internalRegistry = new InterfaceRegistry();
    private final InterfaceProvider parentProvider;
    private final List<Component> childComponents = new LinkedList<Component>();

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

    protected <T> T aquireChildComponentInterface(Component childComponent, Class<T> providedClass) throws NoSuchprovidedInterfaceException {
        if (!this.childComponents.contains(childComponent)) {
            this.childComponents.add(childComponent);
        }
        
        return childComponent.getProvidedInterface(providedClass);
    }
}
