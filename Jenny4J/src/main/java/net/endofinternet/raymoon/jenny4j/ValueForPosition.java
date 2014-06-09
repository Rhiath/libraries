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
public class ValueForPosition {

    private final int position;
    private final int value;

    public ValueForPosition(int position, int value) {
        this.position = position;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.position;
        hash = 43 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValueForPosition other = (ValueForPosition) obj;
        if (this.position != other.position) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

}
