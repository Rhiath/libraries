/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic.implementation;

import java.util.List;
import net.endofinternet.raymoon.automaton.deterministic.Transition;
import net.endofinternet.raymoon.automaton.deterministic.TransitionTable;
import net.endofinternet.raymoon.automaton.nondeterministic.implementation.NonDeterministicTransitionTableImpl;
import net.endofinternet.raymoon.lib.Generic;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;
import net.endofinternet.raymoon.lib.exceptions.InvalidContentException;

/**
 *
 * @author raymoon
 */
public class TransitionTableImpl implements TransitionTable {

    private final NonDeterministicTransitionTableImpl transitionTable;

    public TransitionTableImpl(Transition... transitions) throws InvalidValueContentException {
        transitionTable = new NonDeterministicTransitionTableImpl(Generic.asList(transitions));

        for (Transition transition : transitions) {
            List<Integer> resultingStates = transitionTable.getResultingStates(transition.getOriginalState(), transition.getBySymbol());

            if (resultingStates.size() != 1) {
                throw new InvalidValueContentException("expected one transition rule from state " + transition.getOriginalState() + " by symbol " + transition.getBySymbol());
            }
        }
    }

    @Override
    public int getResultingState(int currentState, int inputSymbol) throws InvalidContentException {
        return transitionTable.getResultingStates(currentState, inputSymbol).get(0);
    }
}
