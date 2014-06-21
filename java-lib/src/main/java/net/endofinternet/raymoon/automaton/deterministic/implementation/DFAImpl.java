/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic.implementation;

import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.automaton.deterministic.DFA;
import net.endofinternet.raymoon.automaton.deterministic.TransitionTable;

/**
 *
 * @author raymoon
 */
public class DFAImpl implements DFA {

    private final TransitionTable transitionTable;
    private final List<Integer> acceptStates;
    private int currentState;

    public DFAImpl(int initialState, TransitionTable transitionTable, List<Integer> acceptStates) {
        this.transitionTable = transitionTable;
        this.currentState = initialState;
        this.acceptStates = acceptStates;
    }

    @Override
    public int getCurrentState() {
        return currentState;
    }

    @Override
    public boolean isInAcceptState() {
        return acceptStates.contains(currentState);
    }

    @Override
    public void consumeInput(int input) {
        currentState = transitionTable.getResultingState(currentState, input);
    }

    @Override
    public void setState(int state) {
        this.currentState = state;
    }

    @Override
    public List<Integer> getAcceptStates() {
       return new LinkedList<Integer>(this.acceptStates);
    }
}
