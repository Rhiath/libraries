/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic.implementation;

import java.util.List;
import net.endofinternet.raymoon.automaton.deterministic.DFA;
import net.endofinternet.raymoon.automaton.deterministic.StateTransitionErrorHandler;
import net.endofinternet.raymoon.automaton.deterministic.Transition;

/**
 *
 * @author raymoon
 */
public class DFAImpl implements DFA {

    private final StateTransitionErrorHandler errorHandler;
    private final List<Transition> transistions;
    private final List<Integer> acceptStates;
    private int currentState;

    public DFAImpl(int initialState, StateTransitionErrorHandler errorHandler, List<Transition> transistions, List<Integer> acceptStates) {
        this.errorHandler = errorHandler;
        this.transistions = transistions;
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
        boolean knownTransition = false;
        int resultingState = currentState;

        for (Transition t : transistions) {
            if (t.getOriginalState() == currentState && t.getBySymbol() == input) {
                knownTransition = true;
                resultingState = t.getResultingState();
            }
        }

        if (!knownTransition) {
            errorHandler.undefinedTransitionEncountered(currentState, input);
        }

        currentState = resultingState;
    }
}
