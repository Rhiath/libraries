/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author raymoon
 */
public abstract class Generic {
    
    /**
     * converts a collection into an array of the specified data type. The collection
     * itself shall be of the defined class type and may contain also elements of
     * subclasses
     * 
     * @param <T> the class type of the collection and the generated array
     * @param values the collection that shall be converted into an array. May be null
     * @param theClass the class type of the collection and the array
     * @return the array representation of the collection or null if provided value is null
     */
    public static <T> T[] toArray(Collection<T> values, Class<? extends T> theClass) {
        T[] retValue = null;

        if (values != null) {
            retValue = (T[]) Array.newInstance(theClass, values.size());

            int position = 0;
            for (T value : values) {
                retValue[position++] = value;
            }
        }

        return retValue;
    }

    /**
     * converts an array into a list.
     * 
     * @param <T> the class type of the array and the generated array
     * @param values the array that shall be converted into a list. May be null
     * @return the list representation of the array or null if the provided value is null
     */
    public static <T> List<T> toList(T... values) {
        List<T> retValue = null;

        if (values != null) {
            retValue = Arrays.asList(values);
        }

        return retValue;
    }
}
