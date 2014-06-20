/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface NonDeterministicTransitionTable {

    public List<Integer> getResultingStates(int currentState, int inputSymbol);
}
