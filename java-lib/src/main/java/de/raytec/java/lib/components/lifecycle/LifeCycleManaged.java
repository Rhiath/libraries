/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components.lifecycle;

import de.raytec.java.lib.components.lifecycle.exceptions.DisposeFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.InitializationFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.InvalidStateTransitionException;
import de.raytec.java.lib.components.lifecycle.exceptions.PauseFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.StartFailedException;

/**
 *
 * @author raymoon
 */
public interface LifeCycleManaged {
    public void init() throws InvalidStateTransitionException, InitializationFailedException;
    public void resume() throws InvalidStateTransitionException, StartFailedException;
    public void suspend() throws InvalidStateTransitionException, PauseFailedException;
    public void dispose() throws InvalidStateTransitionException, PauseFailedException, DisposeFailedException;
}
