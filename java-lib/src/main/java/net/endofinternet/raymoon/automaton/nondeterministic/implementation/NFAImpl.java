/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.automaton.nondeterministic.NFA;
import net.endofinternet.raymoon.automaton.nondeterministic.NonDeterministicTransitionTable;

/**
 *
 * @author raymoon
 */
public class NFAImpl implements NFA {

    private final List<Integer> currentStates = new LinkedList<Integer>();
    private final NonDeterministicTransitionTable transitionTable;
    private final List<Integer> acceptStates;

    public NFAImpl(List<Integer> initialStates, NonDeterministicTransitionTable transitionTable, List<Integer> acceptStates) {
        this.transitionTable = transitionTable;
        this.acceptStates = acceptStates;
        this.currentStates.addAll(initialStates);
    }

    @Override
    public List<Integer> getCurrentStates() {
        return new LinkedList<Integer>(currentStates);
    }

    @Override
    public void consumeInput(int input) {
        List<Integer> newStates = new LinkedList<Integer>();
        
        for ( Integer currentState : currentStates ){
            newStates.addAll(transitionTable.getResultingStates(currentState, input));
        }
        
        this.currentStates.clear();
        this.currentStates.addAll(newStates);
    }

    @Override
    public boolean isInAcceptState() {
        boolean isAcceptState = false;
        
        for ( int currentState : currentStates ){
            if ( acceptStates.contains(currentState)){
                isAcceptState = true;
            }
        }
        
        return isAcceptState;
    }
}
