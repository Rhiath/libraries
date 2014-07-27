/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Ray
 */
public interface ProtocolHandler {

    public void handle(ObjectInputStream ois, ObjectOutputStream oos) throws IOException, ClassNotFoundException;

}
