/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.storageeventtreetest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raymoon
 */
public class ServiceProviderLogger implements ServiceInterface {

    private final ServiceInterface handler;

    public ServiceProviderLogger(ServiceInterface handler) {
        this.handler = handler;
    }

    @Override
    public String getRootValue() {
        String retValue = handler.getRootValue();
                System.out.println("called 'getRootValue' --> " + retValue);
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ServiceProviderLogger.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return retValue;
    }

    @Override
    public List<String> getNodeChildren(String value) {
        System.out.println("called 'getNodeChildren("+value+")'");
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ServiceProviderLogger.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return handler.getNodeChildren(value);
    }

    @Override
    public void put(String id) {
        handler.put(id);
    }

    @Override
    public void remove(String id) {
        handler.remove(id);
    }
}
