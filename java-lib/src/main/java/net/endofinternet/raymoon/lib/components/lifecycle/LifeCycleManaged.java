/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.components.lifecycle;

import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.ConfigurationFailedException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.DisposeFailedException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.InvalidStateTransitionException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.PauseFailedException;
import net.endofinternet.raymoon.lib.components.lifecycle.exceptions.StartFailedException;
import net.endofinternet.raymoon.lib.config.ConfigProvider;

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