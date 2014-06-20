/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.util.List;
import net.endofinternet.raymoon.automaton.nondeterministic.ClosureProvider;
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
        when(closureProvider.getClosure(anyInt())).thenReturn(Generic.asList(5));

        NFAImpl implementation = new NFAImpl(Generic.asList(2, 3), transitionTable, Generic.asList(1), closureProvider);

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
}