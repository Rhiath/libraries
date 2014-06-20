/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic;

import net.endofinternet.raymoon.automaton.AbstractTransition;

/**
 *
 * @author raymoon
 */
public class EpsilonTransition extends AbstractTransition {

    public EpsilonTransition(int originalState, int resultingState) {
        super(originalState, resultingState);
    }
}
