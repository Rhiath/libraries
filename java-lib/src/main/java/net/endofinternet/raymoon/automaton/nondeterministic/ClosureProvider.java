/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic;

import java.util.Collection;

/**
 *
 * @author raymoon
 */
public interface ClosureProvider {

    public Collection<Integer> getClosure(Integer state);
    
}
