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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.bySymbol;
        hash = 83 * hash + super.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transition other = (Transition) obj;
        if (this.bySymbol != other.bySymbol) {
            return false;
        }
        if (this.originalState != other.originalState) {
            return false;
        }
        if (this.resultingState != other.resultingState) {
            return false;
        }
        return true;
    }
}
