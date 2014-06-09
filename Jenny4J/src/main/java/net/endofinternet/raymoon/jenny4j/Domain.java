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
public class Domain {

    private final int position;
    private final int numberOfPossibleValues;

    public Domain(int position, int numberOfPossibleValues) {
        this.position = position;
        this.numberOfPossibleValues = numberOfPossibleValues;
    }

    public int getPosition() {
        return position;
    }

    public int getNumberOfPossibleValues() {
        return numberOfPossibleValues;
    }

}
