/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.components.lifecycle;

import de.raytec.java.lib.components.lifecycle.exceptions.ConfigurationFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.DisposeFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.InitializationFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.InvalidStateTransitionException;
import de.raytec.java.lib.components.lifecycle.exceptions.PauseFailedException;
import de.raytec.java.lib.components.lifecycle.exceptions.StartFailedException;
import de.raytec.java.lib.config.ConfigProvider;

/**
 *
 * @author raymoon
 */
public interface LifeCycleManaged {
    public void resume() throws InvalidStateTransitionException, StartFailedException;
    public void suspend() throws InvalidStateTransitionException, PauseFailedException;
    public void dispose() throws InvalidStateTransitionException, PauseFailedException, DisposeFailedException;
    public void applyConfiguration(ConfigProvider newConfig) throws InvalidStateTransitionException, ConfigurationFailedException;
}

/**
 * CREATED --(applyConfiguration)--> PAUSED
 * RUNNING --(suspend)--> SUSPENDED
 * SUSPENDED --(applyConfiguration)--> SUSPENDED
 * SUSPENDED --(resume)--> RUNNING
 * SUSPENDED --(dispose)--> DISPOSED
 */