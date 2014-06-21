/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic;

import java.util.LinkedList;
import net.endofinternet.raymoon.automaton.nondeterministic.ClosureProvider;
import net.endofinternet.raymoon.automaton.nondeterministic.EpsilonTransition;
import net.endofinternet.raymoon.automaton.nondeterministic.NFA;
import net.endofinternet.raymoon.automaton.nondeterministic.NonDeterministicTransitionTable;
import net.endofinternet.raymoon.automaton.nondeterministic.implementation.ClosureProviderImpl;
import net.endofinternet.raymoon.automaton.nondeterministic.implementation.NFAImpl;
import net.endofinternet.raymoon.automaton.nondeterministic.implementation.NonDeterministicTransitionTableImpl;
import net.endofinternet.raymoon.lib.Generic;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author raymoon
 */
public class DFABuilderTest {

    public DFABuilderTest() {
    }

    @Test
    public void testComputeDFA() throws Exception {
        // transition table for "last 3 characters were '1'", accept state is 3, state 4 is error state
        NonDeterministicTransitionTable transitionTable = new NonDeterministicTransitionTableImpl(Generic.asList(
                new Transition(0, 0, 0),
                new Transition(0, 0, 1),
                new Transition(0, 1, 1),
                new Transition(1, 2, 1),
                new Transition(2, 3, 1),
                new Transition(3, 4, 0),
                new Transition(3, 4, 1),
                new Transition(1, 4, 0),
                new Transition(2, 4, 0),
                new Transition(4, 4, 0),
                new Transition(4, 4, 1)));
        ClosureProvider closure = new ClosureProviderImpl();

        NFA nfa = new NFAImpl(Generic.asList(0), transitionTable, Generic.asList(3), closure);
        DFA dfa = new DFABuilder(nfa).computeDFA();

        for (int numberOfZeros = 0; numberOfZeros < 4; numberOfZeros++) {
            for (int i = 0; i < numberOfZeros; i++) {
                dfa.consumeInput(0);
                assertThat(dfa.isInAcceptState(), is(false));
            }

            dfa.consumeInput(1);
            assertThat(dfa.isInAcceptState(), is(false));
            dfa.consumeInput(1);
            assertThat(dfa.isInAcceptState(), is(false));

            for (int i = 0; i < 4; i++) { // every following character shall result in accept state
                dfa.consumeInput(1);
                assertThat(dfa.isInAcceptState(), is(true));
            }
        }
    }

    @Test
    public void testEpsilons() throws Exception {
        // transition table for " 0* 1* 0* "
        NonDeterministicTransitionTable transitionTable = new NonDeterministicTransitionTableImpl(Generic.asList(
                new Transition(0, 0, 0),
                new Transition(0, 3, 1),
                new Transition(1, 3, 0),
                new Transition(1, 1, 1),
                new Transition(2, 2, 0),
                new Transition(2, 3, 1),
                new Transition(3, 3, 0),
                new Transition(3, 3, 1)));
        ClosureProvider closure = new ClosureProviderImpl(new EpsilonTransition(0, 1), new EpsilonTransition(1, 2));

        NFA nfa = new NFAImpl(Generic.asList(0), transitionTable, Generic.asList(3), closure);

        DFA dfa = new DFABuilder(nfa).computeDFA();
        assertThat(dfa.isInAcceptState(), is(true));

        for (int i = 0; i < 4; i++) {
            dfa.consumeInput(0);
            assertThat(dfa.isInAcceptState(), is(true));
        }

        for (int i = 0; i < 4; i++) {
            dfa.consumeInput(1);
            assertThat(dfa.isInAcceptState(), is(true));
        }

        for (int i = 0; i < 4; i++) {
            dfa.consumeInput(0);
            assertThat(dfa.isInAcceptState(), is(true));
        }
        // from here on, all input will result in invalid states

        for (int i = 0; i < 4; i++) {
            dfa.consumeInput(1);
            assertThat(dfa.isInAcceptState(), is(false));
        }

        for (int i = 0; i < 4; i++) {
            dfa.consumeInput(0);
            assertThat(dfa.isInAcceptState(), is(false));
        }

    }
}