/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic.implementation;

import java.util.Arrays;
import net.endofinternet.raymoon.automaton.deterministic.Transition;
import net.endofinternet.raymoon.automaton.deterministic.TransitionTable;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;

/**
 *
 * @author raymoon
 */
public class TransitionTableImpl implements TransitionTable {
    private static int UNDEFIED_TRANSITION = -1;

    private final int[][] table;

    public TransitionTableImpl(Transition... transitions) throws InvalidValueContentException {
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

        if (transitions.length != (maxState+1) * (maxSymbol+1)) {
            throw new InvalidValueContentException("missmatch in number of transistions, expected " + (maxState * maxSymbol) + " transitions, endountered " + transitions.length);
        }

        table = new int[maxState+1][maxSymbol+1];
        markAllTransitionsAsUndefined();
        applyTransitions(transitions, table);
        rejectUndefinedTransitions();

    }

    @Override
    public int getResultingState(int currentState, int inputSymbol) throws InvalidContentException {
        if (currentState >= table.length) {
            throw new InvalidContentException("state not known by transition table");
        }
        if (inputSymbol >= table[currentState].length) {
            throw new InvalidContentException("symbol not known by transition table");
        }

        return table[currentState][inputSymbol];
    }

    private void rejectUndefinedTransitions() throws InvalidValueContentException {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                if (table[i][j] == -1) {
                    throw new InvalidValueContentException("transition from state " + i + " by symbol " + j + " is undefined");
                }
            }
        }
    }

    private static void applyTransitions(Transition[] transitions, int[][] transitionTable) {
        for (Transition t : transitions) {
            if ( transitionTable[t.getOriginalState()][t.getBySymbol()] != UNDEFIED_TRANSITION ){
                throw new InvalidContentException("encountered duplicate transition for state "+t.getOriginalState()+" by symbol "+t.getBySymbol());
            }
            transitionTable[t.getOriginalState()][t.getBySymbol()] = t.getResultingState();
        }
    }

    private void markAllTransitionsAsUndefined() {
        for (int[] tableLine : table) {
            Arrays.fill(tableLine, UNDEFIED_TRANSITION);
        }
    }
}
