/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components.lifecycle;

import de.raytec.java.lib.components.lifecycle.exceptions.InvalidStateTransitionException;
import de.raytec.java.lib.Generic;
import de.raytec.java.lib.components.lifecycle.exceptions.ConfigurationFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.DisposeFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.InitializationFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.PauseFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.StartFailedException;
import de.raytec.java.lib.config.ConfigProvider;
import de.raytec.java.lib.documentation.SoftwarePattern;

/**
 *
 * @author raymoon
 */
public abstract class AbstractLifeCycleManaged implements LifeCycleManaged {

    private ConfigProvider configProvider = null;
    private State state = State.CREATED;

    public final void applyConfiguration(ConfigProvider newConfig) throws InvalidStateTransitionException, ConfigurationFailedException {
        permissiveCurrentState(new State[]{State.CREATED, State.SUSPENDED}, State.SUSPENDED);

        if (Generic.equals(this.state, State.SUSPENDED)) {
            if (requiresReconfiguration(newConfig)) {
                doApplyConfiguration(newConfig);
            }
            state = State.SUSPENDED;
        }
        if (Generic.equals(this.state, State.CREATED)) {
            doApplyConfiguration(newConfig);
            state = State.SUSPENDED;
        }
        configProvider = newConfig;
    }

    protected abstract void doApplyConfiguration(ConfigProvider newConfig) throws ConfigurationFailedException;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void resume() throws InvalidStateTransitionException, StartFailedException {
        permissiveCurrentState(new State[]{State.SUSPENDED}, State.RUNNING);

        doStart();
        state = State.RUNNING;
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doStart() throws StartFailedException;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void suspend() throws InvalidStateTransitionException, PauseFailedException {
        permissiveCurrentState(new State[]{State.RUNNING}, State.SUSPENDED);

        doPause();
        state = State.SUSPENDED;
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doPause() throws PauseFailedException;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void dispose() throws InvalidStateTransitionException, DisposeFailedException, PauseFailedException {
        permissiveCurrentState(new State[]{State.SUSPENDED, State.RUNNING}, State.TERMINATED);

        if (Generic.equals(this.state, State.RUNNING)) {
            doPause();
            state = State.SUSPENDED;
        }
        if (Generic.equals(this.state, State.SUSPENDED)) {
            doDispose();
            state = State.TERMINATED;
        }
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doDispose() throws DisposeFailedException;

    private void permissiveCurrentState(State[] permittedCurrentStates, State targetState) throws InvalidStateTransitionException {
        if (!Generic.asList(permittedCurrentStates).contains(this.state)) {
            throw new InvalidStateTransitionException("current state '" + this.state.name() + "' is not permitted to be changed to '" + targetState.name() + "'");
        }
    }

    protected ConfigProvider getConfigProvider(){
        return configProvider;
    }
    
    protected abstract boolean requiresReconfiguration(ConfigProvider newConfig);
    
    private static enum State {

        CREATED, RUNNING, SUSPENDED, TERMINATED
    }
}