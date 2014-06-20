/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

import net.endofinternet.raymoon.automaton.AbstractTransition;

/**
 *
 * @author raymoon
 */
public class Transition extends AbstractTransition {
    
    private final int bySymbol;
    
    public Transition(int originalState, int resultingState, int bySymbol) {
        super(originalState, resultingState);
        this.bySymbol = bySymbol;
    }
    
    public int getBySymbol() {
        return bySymbol;
    }
}
