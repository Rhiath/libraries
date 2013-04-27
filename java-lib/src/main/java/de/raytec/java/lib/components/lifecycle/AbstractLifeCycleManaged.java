/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components.lifecycle;

import de.raytec.java.lib.components.lifecycle.exceptions.InvalidStateTransitionException;
import de.raytec.java.lib.Generic;
import de.raytec.java.lib.components.lifecycle.exceptions.DisposeFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.InitializationFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.PauseFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.StartFailedException;
import de.raytec.java.lib.doumentation.SoftwarePattern;

/**
 *
 * @author raymoon
 */
public abstract class AbstractLifeCycleManaged implements LifeCycleManaged {

    private State state = State.CREATED;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void init() throws InvalidStateTransitionException, InitializationFailedException {
        permissiveCurrentState(new State[]{State.CREATED}, State.INITIALIZED);

        doInit();
        state = State.INITIALIZED;
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doInit() throws InitializationFailedException;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void resume() throws InvalidStateTransitionException, StartFailedException {
        permissiveCurrentState(new State[]{State.INITIALIZED, State.SUSPENDED}, State.RUNNING);

        doStart();
        state = State.RUNNING;
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doStart() throws StartFailedException;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void suspend() throws InvalidStateTransitionException, PauseFailedException {
        permissiveCurrentState(new State[]{State.RUNNING, State.INITIALIZED}, State.SUSPENDED);

        if (Generic.equals(this.state, State.RUNNING)) {
            doPause();
        }
        state = State.SUSPENDED;
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doPause() throws PauseFailedException;

    @SoftwarePattern(name = "Template Method", roles = {})
    public void dispose() throws InvalidStateTransitionException, DisposeFailedException, PauseFailedException {
        permissiveCurrentState(new State[]{State.SUSPENDED, State.INITIALIZED, State.RUNNING}, State.TERMINATED);

        if (Generic.equals(this.state, State.RUNNING)) {
            doPause();
        }
        if (Generic.equals(this.state, State.SUSPENDED)) {
            doDispose();
        }
        state = State.TERMINATED;
    }

    @SoftwarePattern(name = "Template Method", roles = {})
    protected abstract void doDispose() throws DisposeFailedException;

    private void permissiveCurrentState(State[] permittedCurrentStates, State targetState) throws InvalidStateTransitionException {
        if (!Generic.asList(permittedCurrentStates).contains(this.state)) {
            throw new InvalidStateTransitionException("current state '" + this.state.name() + "' is not permitted to be changed to '" + targetState.name() + "'");
        }
    }

    private static enum State {

        CREATED, INITIALIZED, SUSPENDED, RUNNING, TERMINATED
    }
}
