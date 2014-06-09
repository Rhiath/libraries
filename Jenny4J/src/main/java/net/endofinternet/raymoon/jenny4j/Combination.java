/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Ray
 */
public class Combination {

    private final Set<ValueForPosition> values;
    private final Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    public Combination(Set<ValueForPosition> values) {
        this.values = new HashSet<ValueForPosition>(values);

        for (ValueForPosition value : values) {
            map.put(value.getPosition(), value.getValue());
        }
    }

    public Set<ValueForPosition> getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.values != null ? this.values.hashCode() : 0);
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
        final Combination other = (Combination) obj;
        if (this.map != other.map && (this.map == null || !this.map.equals(other.map))) {
            return false;
        }
        return true;
    }

}
