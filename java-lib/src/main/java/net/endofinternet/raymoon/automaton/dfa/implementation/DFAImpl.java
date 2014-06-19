/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.dfa.implementation;

import java.util.List;
import net.endofinternet.raymoon.automaton.dfa.DFA;

/**
 *
 * @author raymoon
 */
public class DFAImpl implements DFA {

    private int currentState;
    private final List<Transition> transistions;

    public DFAImpl(int startState, List<Transition> transistions) {
        this.transistions = transistions;
        this.currentState = startState;
    }

    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public void transit(int input) {
        int resultingState = this.currentState;

        for (Transition t : transistions) {
            if (t.getState() == this.currentState && t.getSymbol() == input) {
            }
        }

        this.currentState = resultingState;
    }
}
