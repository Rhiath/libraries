/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools.remoteOperations;

import net.endofinternet.raymoon.gridtools.Sender;
import java.lang.reflect.Method;

/**
 *
 * @author raymoon
 */
public class ProxyListener implements java.lang.reflect.InvocationHandler {

    private final Sender sender;
    
    public ProxyListener(Sender sender) {
        this.sender = sender;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        return null;
    }
}