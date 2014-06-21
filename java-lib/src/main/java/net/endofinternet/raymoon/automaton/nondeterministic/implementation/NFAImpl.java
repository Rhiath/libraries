/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.util.HashSet;
import net.endofinternet.raymoon.automaton.nondeterministic.ClosureProvider;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
    private final ClosureProvider closureProvider;
    
    public NFAImpl(List<Integer> initialStates, NonDeterministicTransitionTable transitionTable, List<Integer> acceptStates, ClosureProvider closureProvider) {
        this.transitionTable = transitionTable;
        this.acceptStates = acceptStates;
        this.closureProvider = closureProvider;
        
        Set<Integer> newStatesWithClosure = getStatesIncludingClosure(new HashSet(initialStates));   
        this.currentStates.clear();
        this.currentStates.addAll(newStatesWithClosure);
    }
    
    @Override
    public List<Integer> getCurrentStates() {
        return new LinkedList<Integer>(currentStates);
    }
    
    @Override
    public void consumeInput(int input) {
        Set<Integer> newStates = new HashSet<Integer>();
        
        for (Integer currentState : currentStates) {
            newStates.addAll(transitionTable.getResultingStates(currentState, input));
        }
        Set<Integer> newStatesWithClosure = getStatesIncludingClosure(newStates);
        
        this.currentStates.clear();
        this.currentStates.addAll(newStatesWithClosure);
    }
    
    @Override
    public boolean isInAcceptState() {
        boolean isAcceptState = false;
        
        for (int currentState : currentStates) {
            if (acceptStates.contains(currentState)) {
                isAcceptState = true;
            }
        }
        
        return isAcceptState;
    }

    @Override
    public void setStates(List<Integer> states) {
        this.currentStates.clear();
        this.currentStates.addAll(states);
    }

    @Override
    public List<Integer> getAcceptStates() {
       return new LinkedList<Integer>(acceptStates);
    }

    @Override
    public int getInputValueLimit() {
       return transitionTable.getInputValueLimit();
    }

    private Set<Integer> getStatesIncludingClosure(Set<Integer> newStates) {
        Set<Integer> newStatesWithClosure = new HashSet<Integer>(newStates);
        for (Integer newState : newStates) {
            newStatesWithClosure.addAll(closureProvider.getClosure(newState));
        }
        return newStatesWithClosure;
    }
}
