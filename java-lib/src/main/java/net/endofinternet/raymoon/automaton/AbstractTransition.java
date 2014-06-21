/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton;

/**
 *
 * @author raymoon
 */
public abstract class AbstractTransition {

    protected final int originalState;
    protected final int resultingState;

    public AbstractTransition(int originalState, int resultingState) {
        this.originalState = originalState;
        this.resultingState = resultingState;
    }

    public int getOriginalState() {
        return originalState;
    }

    public int getResultingState() {
        return resultingState;
    }
    }
