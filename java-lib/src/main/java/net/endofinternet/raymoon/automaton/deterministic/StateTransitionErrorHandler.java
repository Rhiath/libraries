/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

/**
 *
 * @author raymoon
 */
public interface StateTransitionErrorHandler {
    public void undefinedTransitionEncountered(int state, int inut);
}
