/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

import net.endofinternet.raymoon.automaton.Automaton;

/**
 *
 * @author raymoon
 */
public interface DFA extends Automaton {
    public void setState(int state);

    public int getCurrentState();
}
