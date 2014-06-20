/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.endofinternet.raymoon.automaton.nondeterministic.ClosureProvider;
import net.endofinternet.raymoon.automaton.nondeterministic.EpsilonTransition;

/**
 *
 * @author raymoon
 */
public class ClosureProviderImpl implements ClosureProvider {
    
    private final Map<Integer, Set<Integer>> closure = new HashMap<Integer, Set<Integer>>();
    
    public ClosureProviderImpl(EpsilonTransition... transitions) {
        for (EpsilonTransition transition : transitions) {
            if (!closure.containsKey(transition.getOriginalState())) {
                closure.put(transition.getOriginalState(), new HashSet<Integer>());
            }
            
            closure.get(transition.getOriginalState()).add(transition.getResultingState());
        }
        
        closeClosureLookup();
    }
    
    @Override
    public Collection<Integer> getClosure(Integer state) {
        Collection<Integer> retValue;
        
        if (closure.containsKey(state)) {
            retValue = closure.get(state);
        } else {
            retValue = new LinkedList<Integer>();
        }
        
        return retValue;
    }
    
    private void closeClosureLookup() {
        for (Integer id : closure.keySet()) {
            closure.get(id).addAll(getTransitiveClosure(id));
        }
    }
    
    private Collection<Integer> getTransitiveClosure(Integer id) {
        Set<Integer> retValue = new HashSet<Integer>();
        List<Integer> unclosedClosure = new LinkedList<Integer>();
        unclosedClosure.addAll(getClosure(id));
        
        while (!unclosedClosure.isEmpty()) {
            Integer closureID = unclosedClosure.remove(0);
            retValue.add(closureID);
            
            for (Integer resultintClosureID : getClosure(closureID)) {
                if (!retValue.contains(resultintClosureID) && !unclosedClosure.contains(resultintClosureID)) {
                    unclosedClosure.add(resultintClosureID);
                }
            }
        }
        
        return retValue;
    }
}
