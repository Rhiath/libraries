/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.dfa.implementation;

/**
 *
 * @author raymoon
 */
public class Transition {

    private final int state;
    private final int symbol;
    private final int resultingState;

    public Transition(int state, int symbol, int resultingState) {
        this.state = state;
        this.symbol = symbol;
        this.resultingState = resultingState;
    }

    public int getState() {
        return state;
    }

    public int getSymbol() {
        return symbol;
    }

    public int getResultingState() {
        return resultingState;
    }
}
