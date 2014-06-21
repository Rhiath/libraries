/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.automaton.deterministic.Transition;
import net.endofinternet.raymoon.automaton.nondeterministic.NonDeterministicTransitionTable;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;

/**
 *
 * @author raymoon
 */
public class NonDeterministicTransitionTableImpl implements NonDeterministicTransitionTable {

    private final List<Integer>[][] table;
    private final int inputValueLimit;

    public NonDeterministicTransitionTableImpl(List<Transition> transitions) throws InvalidValueContentException {
        int maxState = 0;
        int maxSymbol = 0;

        for (Transition t : transitions) {
            if (t.getOriginalState() < 0) {
                throw new InvalidValueContentException("negative state IDs are not permitted");
            }
            if (t.getResultingState() < 0) {
                throw new InvalidValueContentException("negative state IDs are not permitted");
            }

            maxState = Math.max(t.getOriginalState(), maxState);
            maxState = Math.max(t.getResultingState(), maxState);
            maxSymbol = Math.max(t.getBySymbol(), maxSymbol);
        }
        this.inputValueLimit = maxSymbol + 1;

        if (transitions.size() < (maxState + 1) * (maxSymbol + 1)) {
            throw new InvalidValueContentException("missmatch in number of transistions, expected at least " + (maxState * maxSymbol) + " transitions, endountered " + transitions.size());
        }

        table = (List<Integer>[][]) Array.newInstance(List.class, maxState + 1, maxSymbol + 1);
        markAllTransitionsAsUndefined();
        transitions = new LinkedList<Transition>(transitions);
        applyTransitions(transitions, table);
        rejectUndefinedTransitions();

    }

    @Override
    public List<Integer> getResultingStates(int currentState, int inputSymbol) throws InvalidContentException {
        if (currentState >= table.length) {
            throw new InvalidContentException("state not known by transition table");
        }
        if (inputSymbol >= table[currentState].length) {
            throw new InvalidContentException("symbol not known by transition table");
        }

        return new LinkedList<Integer>(table[currentState][inputSymbol]);
    }

    private void rejectUndefinedTransitions() throws InvalidValueContentException {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j].isEmpty()) {
                    throw new InvalidValueContentException("transition from state " + i + " by symbol " + j + " is undefined");
                }
            }
        }
    }

    private static void applyTransitions(List<Transition> transitions, List<Integer>[][] transitionTable) throws InvalidValueContentException {
        for (Transition t : transitions) {
            if (transitionTable[t.getOriginalState()][t.getBySymbol()].contains(t.getResultingState())) {
                throw new InvalidValueContentException("encountered duplicate transition for state " + t.getOriginalState() + " by symbol " + t.getBySymbol() + " go state " + t.getResultingState());
            }
            transitionTable[t.getOriginalState()][t.getBySymbol()].add(t.getResultingState());
        }
    }

    private void markAllTransitionsAsUndefined() {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = new LinkedList<Integer>();
            }

        }
    }

    @Override
    public int getInputValueLimit() {
       return inputValueLimit;
    }
}
