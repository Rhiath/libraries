/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

/**
 *
 * @author raymoon
 */
public class Transition {

    private final int originalState;
    private final int resultingState;
    private final int bySymbol;

    public Transition(int originalState, int resultingState, int bySymbol) {
        this.originalState = originalState;
        this.resultingState = resultingState;
        this.bySymbol = bySymbol;
    }

    public int getOriginalState() {
        return originalState;
    }

    public int getResultingState() {
        return resultingState;
    }

    public int getBySymbol() {
        return bySymbol;
    }
}
