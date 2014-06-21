/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface Automaton {

    void consumeInput(int input);

    public List<Integer> getAcceptStates();
    
    boolean isInAcceptState();
    
}
