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
    public static <T> T[] toArray(Collection<T> values, Class<? extends T> theClass){
        T[] retValue = null;
        
        if( values != null ){
            retValue = (T[]) Array.newInstance(theClass, values.size());
           
            int position = 0;
            for ( T value : values ){
                retValue[position++] = value;
            }
        }
        
        return retValue;
    }
    
    public static <T> List<T> toList(T... values ){
        List<T> retValue = null;
        
        if ( values != null ){
            retValue = Arrays.asList(values);
        }
        
        return retValue;
    }
}
