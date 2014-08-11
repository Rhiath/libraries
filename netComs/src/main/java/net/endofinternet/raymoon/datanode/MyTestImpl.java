/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

import net.endofinternet.raymoon.datanode.App.TestInterface;

/**
 *
 * @author raymoon
 */
public class MyTestImpl implements TestInterface {

    public MyTestImpl() {
    }

    @Override
    public int halloWelt() throws Exception {
        System.out.println("hallo welt!");
        throw new Exception("something bad happened!");
//        return 42;
    }

    @Override
    public void anotherMethod() {
        System.out.println("another method!");
    }
    
}
