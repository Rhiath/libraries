/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.endofinternet.raymoon.automaton.deterministic.implementation.DFAImpl;
import net.endofinternet.raymoon.automaton.deterministic.implementation.TransitionTableImpl;
import net.endofinternet.raymoon.automaton.nondeterministic.NFA;
import net.endofinternet.raymoon.lib.Generic;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;

/**
 *
 * @author raymoon
 */
public class DFABuilder {

    private final Set<Integer> startingState;
    private final NFA nfaToConvert;
    private final Map<Set<Integer>, Integer> stateMapping = new HashMap<Set<Integer>, Integer>();
    private final List<Transition> transitions = new LinkedList<Transition>();
    private final Set<Set<Integer>> calculatedStates = new HashSet<Set<Integer>>();

    public DFABuilder(NFA nfaToConvert) {
        this.nfaToConvert = nfaToConvert;
        startingState = new HashSet<Integer>(nfaToConvert.getCurrentStates());
    }

    public DFA computeDFA() throws InvalidValueContentException {
        computeAllInputsForState(startingState);

        return new DFAImpl(getDFAState(startingState), new TransitionTableImpl(Generic.asArray(transitions, Transition.class)), getAcceptStates());
    }

    private void computeAllInputsForState(Set<Integer> currentState) {
        if (calculatedStates.contains(currentState)) {
            return;  // prevent infinite loops
        } else {
            calculatedStates.add(currentState);
        }

        for (int i = 0; i < nfaToConvert.getInputValueLimit(); i++) {
            nfaToConvert.setStates(new LinkedList<Integer>(currentState));
            nfaToConvert.consumeInput(i);
            Set<Integer> states = new HashSet(nfaToConvert.getCurrentStates());

            memorizeTransition(currentState, states, i);

            computeAllInputsForState(states);
        }
    }

    private void memorizeTransition(Set<Integer> startingStates, Set<Integer> resultingStates, int symbol) {
        int dfaSourceState = getDFAState(startingStates);
        int dfaResultingState = getDFAState(resultingStates);

        Transition transition = new Transition(dfaSourceState, dfaResultingState, symbol);

        if (!transitions.contains(transition)) {
            transitions.add(transition);
        }
    }

    private int getDFAState(Set<Integer> stateSet) {
        if (!stateMapping.containsKey(stateSet)) {
            stateMapping.put(stateSet, stateMapping.size());
        }

        return stateMapping.get(stateSet);
    }

    private List<Integer> getAcceptStates() {
        List<Integer> retValue = new LinkedList<Integer>();

        for (Entry<Set<Integer>, Integer> entry : stateMapping.entrySet()) {
            if (Generic.containsAny(entry.getKey(), nfaToConvert.getAcceptStates())) {
                retValue.add(entry.getValue());
            }
        }

        return retValue;
    }
}
