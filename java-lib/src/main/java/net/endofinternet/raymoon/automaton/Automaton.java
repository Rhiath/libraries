/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton;

/**
 *
 * @author raymoon
 */
public interface Automaton {

    void consumeInput(int input);

    boolean isInAcceptState();
    
}
