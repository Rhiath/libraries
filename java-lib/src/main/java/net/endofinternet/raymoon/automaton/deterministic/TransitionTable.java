/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

/**
 *
 * @author raymoon
 */
public interface TransitionTable {
    public int getResultingState(int currentState, int inputSymbol);
}
