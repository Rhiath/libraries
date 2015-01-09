/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.storageeventtreetest;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface ServiceInterface {
    public String getRootValue();
    public List<String> getNodeChildren(String value);
    
    
    public void put(String id);
    
    public void remove(String id);
}
