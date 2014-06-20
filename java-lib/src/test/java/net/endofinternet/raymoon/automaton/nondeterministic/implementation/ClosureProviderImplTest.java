/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.nondeterministic.implementation;

import java.util.Collection;
import net.endofinternet.raymoon.automaton.nondeterministic.EpsilonTransition;
import net.endofinternet.raymoon.lib.Generic;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author raymoon
 */
public class ClosureProviderImplTest {

    public ClosureProviderImplTest() {
    }

    @Test
    public void testGetClosure() {
        ClosureProviderImpl closureProvider = new ClosureProviderImpl(
                new EpsilonTransition(0, 1),
                new EpsilonTransition(0, 2),
                new EpsilonTransition(1, 0),
                new EpsilonTransition(2, 4),
                new EpsilonTransition(4, 0));

        Collection<Integer> closure = closureProvider.getClosure(0);
        assertThat(closure, hasItem(0));
        assertThat(closure, hasItem(1));
        assertThat(closure, hasItem(2));
        assertThat(closure, hasItem(4));
        assertThat(closure.size(), is(4));
    }

    @Test
    public void testGetClosure2() {
        ClosureProviderImpl closureProvider = new ClosureProviderImpl(
                new EpsilonTransition(0, 1),
                new EpsilonTransition(0, 2),
                new EpsilonTransition(1, 0),
                new EpsilonTransition(2, 4),
                new EpsilonTransition(4, 0));

        Collection<Integer> closure = closureProvider.getClosure(3);
        assertThat(closure.size(), is(0));
    }

    @Test
    public void testGetClosure3() {
        ClosureProviderImpl closureProvider = new ClosureProviderImpl(
                new EpsilonTransition(0, 1),
                new EpsilonTransition(0, 2),
                new EpsilonTransition(1, 0),
                new EpsilonTransition(2, 4),
                new EpsilonTransition(4, 0));

        Collection<Integer> closure = closureProvider.getClosure(4);
        assertThat(closure, hasItem(0));
        assertThat(closure, hasItem(1));
        assertThat(closure, hasItem(2));
        assertThat(closure, hasItem(4));
        assertThat(closure.size(), is(4));
    }
}