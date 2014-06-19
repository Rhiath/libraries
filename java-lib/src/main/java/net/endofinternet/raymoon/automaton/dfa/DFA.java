/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.dfa;

/**
 *
 * @author raymoon
 */
public interface DFA {

    public int getCurrentState();

    public void transit(int input);
}
