/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic.implementation;

import net.endofinternet.raymoon.automaton.deterministic.Transition;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author raymoon
 */
public class TransitionTableImplTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getResultingState method, of class TransitionTableImpl.
     */
    @Test
    public void testValidConstructor() throws InvalidValueContentException {
        new TransitionTableImpl(new Transition(0, 0, 0));
    }

    /**
     * Test of getResultingState method, of class TransitionTableImpl.
     */
    @Test(expected = InvalidValueContentException.class)
    public void testDuplicateTransitionWithConstructor() throws InvalidValueContentException {
        new TransitionTableImpl(new Transition(0, 0, 0), new Transition(0, 0, 0));
    }

    /**
     * Test of getResultingState method, of class TransitionTableImpl.
     */
    @Test
    public void testValidWithMultipleStatesAndSymbolsConstructor() throws InvalidValueContentException {
        new TransitionTableImpl(new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 1, 1));
    }

    /**
     * Test of getResultingState method, of class TransitionTableImpl.
     */
    @Test(expected = InvalidValueContentException.class)
    public void testMissingSymbolInConstructor() throws InvalidValueContentException {
        new TransitionTableImpl(new Transition(0, 0, 1));
    }

    /**
     * Test of getResultingState method, of class TransitionTableImpl.
     */
    @Test(expected = InvalidValueContentException.class)
    public void testMissingStateInConstructor() throws InvalidValueContentException {
        new TransitionTableImpl(new Transition(1, 0, 0));
    }

    /**
     * Test of getResultingState method, of class TransitionTableImpl.
     */
    @Test
    public void testTransitions() throws InvalidValueContentException {
        TransitionTableImpl table = new TransitionTableImpl(
                new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 2, 1),
                new Transition(2, 2, 1),
                new Transition(2, 1, 0));

        assertThat(table.getResultingState(0, 0), is(0));
        assertThat(table.getResultingState(0, 1), is(1));
        assertThat(table.getResultingState(1, 0), is(0));
        assertThat(table.getResultingState(1, 1), is(2));
        assertThat(table.getResultingState(2, 0), is(1));
        assertThat(table.getResultingState(2, 1), is(2));
    }
}