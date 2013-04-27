/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author raymoon
 */
public abstract class Generic {

    /**
     * converts a collection into an array of the specified data type. The
     * collection itself shall be of the defined class type and may contain also
     * elements of subclasses
     *
     * @param <T> the class type of the collection and the generated array
     * @param values the collection that shall be converted into an array. May
     * be null
     * @param theClass the class type of the collection and the array
     * @return the array representation of the collection or empty array if provided value is null
     */
    public static <T> T[] asArray(Collection<T> values, Class<? extends T> theClass) {
        T[] retValue;

        if (values != null) {
            retValue = (T[]) Array.newInstance(theClass, values.size());

            int position = 0;
            for (T value : values) {
                retValue[position++] = value;
            }
        } else {
            retValue = (T[]) Array.newInstance(theClass, 0);
        }

        return retValue;
    }

    /**
     * converts an array into a list.
     *
     * @param <T> the class type of the array and the generated list
     * @param values the array that shall be converted into a list. May be null
     * @return the list representation of the array or empty list if the provided
     * value is null
     */
    public static <T> List<T> asList(T... values) {
        List<T> retValue = new LinkedList<T>();

        if (values != null) {
            retValue = Arrays.asList(values);
        }

        return retValue;
    }

    /**
     * converts a set into a list
     * 
     * @param <T>  the class type of the set and the generated list
     * @param values the set that shall be converted into a list
     * @return the list representation of the set or empty list if the provided value is null
     */
    public static <T> List<T> asList(Set<T> values) {
        List<T> retValue = new LinkedList<T>();;

        if (values != null) {
            retValue.addAll(values);
        }

        return retValue;
    }
    
    /**
     * compares two objects for equality. If one of the values is null and the other
     * is not, they are considered unequal. If both of them are null, they are
     * considered equal. If neither of them is null, the object specific equality
     * check is performed
     * 
     * @param a the first object
     * @param b the second object
     * @return true if the two provided values are considered equal
     */
    public static boolean equals(Object a, Object b ){
        boolean retValue = false;
        
        if ( a == null && b == null ){
            retValue = true;
        } else if ( a != null && b != null ){
            retValue = a.equals(b);
        }
        
        return retValue;
    }
}
