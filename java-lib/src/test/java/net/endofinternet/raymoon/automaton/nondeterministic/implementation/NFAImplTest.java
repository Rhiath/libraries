/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.automaton.deterministic.DFA;
import net.endofinternet.raymoon.automaton.deterministic.DFABuilder;
import net.endofinternet.raymoon.automaton.deterministic.Transition;
import net.endofinternet.raymoon.automaton.nondeterministic.ClosureProvider;
import net.endofinternet.raymoon.automaton.nondeterministic.EpsilonTransition;
import net.endofinternet.raymoon.automaton.nondeterministic.NFA;
import net.endofinternet.raymoon.automaton.nondeterministic.NonDeterministicTransitionTable;
import net.endofinternet.raymoon.lib.Generic;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author raymoon
 */
public class NFAImplTest {

    public NFAImplTest() {
    }

    @Test
    public void testGetCurrentStates() {
        NonDeterministicTransitionTable transitionTable = mock(NonDeterministicTransitionTable.class);
        ClosureProvider closureProvider = mock(ClosureProvider.class);

        NFAImpl implementation = new NFAImpl(Generic.asList(2, 3), transitionTable, Generic.asList(1), closureProvider);

        List<Integer> currentStates = implementation.getCurrentStates();
        verify(closureProvider).getClosure(2);
        verify(closureProvider).getClosure(3);
        assertThat(currentStates, hasItem(2));
        assertThat(currentStates, hasItem(3));
        assertThat(currentStates.size(), is(2));

        verifyNoMoreInteractions(transitionTable);
        verifyNoMoreInteractions(closureProvider);
    }

    @Test
    public void testConsumeInput() {
        NonDeterministicTransitionTable transitionTable = mock(NonDeterministicTransitionTable.class);
        ClosureProvider closureProvider = mock(ClosureProvider.class);
        when(transitionTable.getResultingStates(anyInt(), anyInt())).thenReturn(Generic.asList(1));
        when(closureProvider.getClosure(1)).thenReturn(Generic.asList(5));
        when(closureProvider.getClosure(2)).thenReturn(new LinkedList<Integer>());
        when(closureProvider.getClosure(3)).thenReturn(new LinkedList<Integer>());

        NFAImpl implementation = new NFAImpl(Generic.asList(2, 3), transitionTable, Generic.asList(1), closureProvider);

        verify(closureProvider).getClosure(2);
        verify(closureProvider).getClosure(3);
        implementation.consumeInput(4);
        verify(transitionTable).getResultingStates(3, 4);
        verify(transitionTable).getResultingStates(2, 4);
        verify(closureProvider).getClosure(1);

        verifyNoMoreInteractions(transitionTable);
        verifyNoMoreInteractions(closureProvider);

        List<Integer> currentStates = implementation.getCurrentStates();
        assertThat(currentStates, hasItem(5));
        assertThat(currentStates, hasItem(1));
        assertThat(currentStates.size(), is(2));
    }

    @Test
    public void testIsInAcceptStateFalse() {
        NonDeterministicTransitionTable transitionTable = mock(NonDeterministicTransitionTable.class);
        ClosureProvider closureProvider = mock(ClosureProvider.class);
        when(transitionTable.getResultingStates(anyInt(), anyInt())).thenReturn(Generic.asList(1));
        when(closureProvider.getClosure(anyInt())).thenReturn(Generic.asList(5));

        NFAImpl implementation = new NFAImpl(Generic.asList(2, 3), transitionTable, Generic.asList(1), closureProvider);

        assertThat(implementation.isInAcceptState(), is (false));
    }
    
    @Test
    public void testIsInAcceptStateTrue() {
        NonDeterministicTransitionTable transitionTable = mock(NonDeterministicTransitionTable.class);
        ClosureProvider closureProvider = mock(ClosureProvider.class);
        when(transitionTable.getResultingStates(anyInt(), anyInt())).thenReturn(Generic.asList(1));
        when(closureProvider.getClosure(anyInt())).thenReturn(Generic.asList(5));

        NFAImpl implementation = new NFAImpl(Generic.asList(2, 3), transitionTable, Generic.asList(3,5), closureProvider);

        assertThat(implementation.isInAcceptState(), is (true));
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

        NFA nfa = new NFAImpl(Generic.asList(0), transitionTable, Generic.asList(2), closure);

        assertThat(nfa.isInAcceptState(), is(true));

        for (int i = 0; i < 4; i++) {
            nfa.consumeInput(0);
            assertThat(nfa.isInAcceptState(), is(true));
        }

        for (int i = 0; i < 4; i++) {
            nfa.consumeInput(1);
            assertThat(nfa.isInAcceptState(), is(true));
        }

        for (int i = 0; i < 4; i++) {
            nfa.consumeInput(0);
            assertThat(nfa.isInAcceptState(), is(true));
        }
        // from here on, all input will result in invalid states

        for (int i = 0; i < 4; i++) {
            nfa.consumeInput(1);
            assertThat(nfa.isInAcceptState(), is(false));
        }

        for (int i = 0; i < 4; i++) {
            nfa.consumeInput(0);
            assertThat(nfa.isInAcceptState(), is(false));
        }

    }
}