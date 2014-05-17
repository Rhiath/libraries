/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.components;

import net.endofinternet.raymoon.lib.components.lifecycle.AbstractLifeCycleManaged;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.ConfigurationFailedException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.DisposeFailedException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.InvalidStateTransitionException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.PauseFailedException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.StartFailedException;
import net.endofinternet.raymoon.lib.config.ConfigProvider;
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

    public final <T> T getProvidedInterface(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        return externalRegistry.getProvidedInterface(interfaceClass);
    }

    protected final <T> void provideInterfaceExternally(T object, Class<T> providedClass) {
        externalRegistry.provideInterface(object, providedClass);
    }

    protected final <T> void provideInterfaceInternally(T object, Class<T> providedClass) {
        internalRegistry.provideInterface(object, providedClass);
    }

    protected final <T> void provideParentProvidedInterfaceToChildren(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        this.internalRegistry.provideInterface(parentProvider.getProvidedInterface(interfaceClass), interfaceClass);
    }

    protected final <T> T aquireParentProvidedInterface(Class<T> interfaceClass) throws NoSuchprovidedInterfaceException {
        return parentProvider.getProvidedInterface(interfaceClass);
    }

    protected final <T> T aquireChildComponentInterface(Component childComponent, Class<T> providedClass) throws NoSuchprovidedInterfaceException {
        if (!this.childComponents.contains(childComponent)) {
            this.childComponents.add(childComponent);
        }

        return childComponent.getProvidedInterface(providedClass);
    }

    @Override
    protected final void doStart() throws StartFailedException {
        for (Component child : childComponents) {
            try {
                child.resume();
            } catch (InvalidStateTransitionException ex) {
                throw new StartFailedException("failed to start child component", ex);
            }
        }

        doSelfStart();
    }

    @Override
    protected final void doPause() throws PauseFailedException {
        doSelfPause();

        for (Component child : childComponents) {
            try {
                child.suspend();
            } catch (InvalidStateTransitionException ex) {
                throw new PauseFailedException("failed to pause child component", ex);
            }
        }
    }

    @Override
    protected final void doDispose() throws DisposeFailedException {
        doSelfDispose();

        for (Component child : childComponents) {
            try {
                child.dispose();
            } catch (InvalidStateTransitionException ex) {
                throw new DisposeFailedException("failed to dipose child component", ex);
            } catch (PauseFailedException ex) {
                throw new DisposeFailedException("failed to dipose child component", ex);
            }
        }
    }

    @Override
    protected final void doApplyConfiguration(ConfigProvider newConfig) throws ConfigurationFailedException {

        for (Component child : childComponents) {
            try {
                child.applyConfiguration(newConfig);
            } catch (InvalidStateTransitionException ex) {
                throw new ConfigurationFailedException("failed to dipose child component", ex);
            }
        }

        doSelfApplyConfiguration(newConfig);
    }

    protected abstract void doSelfStart();

    protected abstract void doSelfPause();

    protected abstract void doSelfDispose();

    protected abstract void doSelfApplyConfiguration(ConfigProvider newConfig);
}
