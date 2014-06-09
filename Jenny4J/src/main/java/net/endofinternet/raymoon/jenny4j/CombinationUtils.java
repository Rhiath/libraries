/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Ray
 */
public class CombinationUtils {

    public static boolean isDistinct(Combination unaryValues, Combination lastIteration) {
        for (ValueForPosition val1 : unaryValues.getValues()) {
            for (ValueForPosition val2 : lastIteration.getValues()) {
                if (val1.getPosition() == val2.getPosition()) {
                    return false;
                }
            }
        }

        return true;
    }

    public static Combination combine(Combination unaryValues, Combination lastIteration) {
        Set<ValueForPosition> values = new HashSet<ValueForPosition>();
        values.addAll(unaryValues.getValues());
        values.addAll(lastIteration.getValues());

        return new Combination(values);
    }

    static boolean isMergeable(Combination combA, Combination combB) {
        for (ValueForPosition val1 : combA.getValues()) {
            for (ValueForPosition val2 : combB.getValues()) {
                if (val1.getPosition() == val2.getPosition()) {
                    if (val1.getValue() != val2.getValue()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean matches(Combination solution, Combination pattern) {
        Map<Integer, Integer> valueByPositionMap = new HashMap<Integer, Integer>();

        for (ValueForPosition valueByPosition : solution.getValues()) {
            valueByPositionMap.put(valueByPosition.getPosition(), valueByPosition.getValue());
        }
        for (ValueForPosition valueByPosition : pattern.getValues()) {
            try {
                if (valueByPositionMap.get(valueByPosition.getPosition()) != valueByPosition.getValue()) {
                    return false;
                }
            } catch (java.lang.NullPointerException ex) {
                throw ex;
            }
        }

        return true;
    }
}
