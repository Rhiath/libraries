/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.automaton.deterministic.implementation;

import java.util.LinkedList;
import net.endofinternet.raymoon.automaton.deterministic.DFA;
import net.endofinternet.raymoon.automaton.deterministic.Transition;
import net.endofinternet.raymoon.lib.Generic;
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
public class DFAImplTest {

    public DFAImplTest() {
    }

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
     * Test of getCurrentState method, of class DFAImpl.
     */
    @Test
    public void testGetCurrentState() throws InvalidValueContentException {
        TransitionTableImpl table = new TransitionTableImpl(
                new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 2, 1),
                new Transition(2, 2, 1),
                new Transition(2, 1, 0));

        DFA dfa = new DFAImpl(1, table, Generic.asList(2));
        
        assertThat(dfa.getCurrentState(),is(1));
    }
    
    /**
     * Test of getCurrentState method, of class DFAImpl.
     */
    @Test
    public void testGetCurrentState2() throws InvalidValueContentException {
        TransitionTableImpl table = new TransitionTableImpl(
                new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 2, 1),
                new Transition(2, 2, 1),
                new Transition(2, 1, 0));

        DFA dfa = new DFAImpl(2, table, Generic.asList(2));
        
        assertThat(dfa.getCurrentState(),is(2));
    }

    
    
    /**
     * Test of getCurrentState method, of class DFAImpl.
     */
    @Test
    public void testIisInAcceptState1() throws InvalidValueContentException {
        TransitionTableImpl table = new TransitionTableImpl(
                new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 2, 1),
                new Transition(2, 2, 1),
                new Transition(2, 1, 0));

        DFA dfa = new DFAImpl(1, table, Generic.asList(2));
        
        assertFalse(dfa.isInAcceptState());
    }
    
    /**
     * Test of getCurrentState method, of class DFAImpl.
     */
    @Test
    public void testIisInAcceptState2() throws InvalidValueContentException {
        TransitionTableImpl table = new TransitionTableImpl(
                new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 2, 1),
                new Transition(2, 2, 1),
                new Transition(2, 1, 0));

        DFA dfa = new DFAImpl(2, table, Generic.asList(2));
        
        assertTrue(dfa.isInAcceptState());
    }
    
    
    
    /**
     * Test of getCurrentState method, of class DFAImpl.
     */
    @Test
    public void testITransitions() throws InvalidValueContentException {
        TransitionTableImpl table = new TransitionTableImpl(
                new Transition(0, 0, 0),
                new Transition(0, 1, 1),
                new Transition(1, 0, 0),
                new Transition(1, 2, 1),
                new Transition(2, 2, 1),
                new Transition(2, 1, 0));

        DFA dfa = new DFAImpl(0, table, Generic.asList(2));
        
        dfa.consumeInput(0);
        assertThat(dfa.getCurrentState(),is(0));
        dfa.consumeInput(1);
        assertThat(dfa.getCurrentState(),is(1));
        dfa.consumeInput(0);
        assertThat(dfa.getCurrentState(),is(0));
        dfa.consumeInput(1);
        dfa.consumeInput(1);
        assertThat(dfa.getCurrentState(),is(2));
        dfa.consumeInput(0);
        assertThat(dfa.getCurrentState(),is(1));
    }
}