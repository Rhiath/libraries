/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

/**
 *
 * @author Ray
 */
public interface ConstraintChecker {

    /**
     * checks that a built combination is valid according to the criteria for
     * solutions.
     *
     * @param combination the combination to check
     * @return true if valid, false otherwise
     */
    public boolean isValid(Combination combination);
}
