/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import net.endofinternet.raymoon.lib.Generic;
import org.junit.Test;



/**
 *
 * @author raymoon
 */
public class GenericTest {
    @Test
    public void nullListToArray(){
        Assert.assertTrue(Generic.asArray((Collection) null, String.class) != null);
    }
    
    @Test
    public void nonNullListToArray(){
        List<String> values = new LinkedList<String>();
        values.add("hello");
        values.add("world");
        
        String[] converted = Generic.asArray(values, String.class);
        
        Assert.assertTrue(converted != null);
        Assert.assertTrue(converted.length == values.size());
        for (int i = 0; i < converted.length; i++) {
            Assert.assertTrue(converted[i] == values.get(i));
            
        }
    }

    @Test
    public void nullArrayToList(){
        Assert.assertTrue(Generic.asList((Set) null) != null);
    }
    
    @Test
    public void nullValuesToList(){
        List<String> converted = Generic.asList((String) null);
        Assert.assertTrue(converted != null);
    }
    
    @Test
    public void nonNullValuesToList(){
        String[] values = new String[]{"hallo", "welt"};
        List<String> converted = Generic.asList(values);
        Assert.assertTrue(converted != null);
        Assert.assertTrue(converted.size() == values.length);
        for (int i = 0; i < converted.size(); i++) {
            Assert.assertTrue(converted.get(i) == values[i]);
        }
    }
    
}
