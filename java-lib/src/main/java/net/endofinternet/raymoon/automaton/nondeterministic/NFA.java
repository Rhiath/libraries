/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic;

import java.util.List;
import net.endofinternet.raymoon.automaton.Automaton;

/**
 *
 * @author raymoon
 */
public interface NFA extends Automaton {

    public List<Integer> getCurrentStates();
}
