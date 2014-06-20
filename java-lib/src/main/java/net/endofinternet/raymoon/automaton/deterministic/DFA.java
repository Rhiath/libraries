/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

/**
 *
 * @author raymoon
 */
public interface DFA {
    public int getCurrentState();
    
    public boolean isInAcceptState();
    
    public void consumeInput(int input);
}
