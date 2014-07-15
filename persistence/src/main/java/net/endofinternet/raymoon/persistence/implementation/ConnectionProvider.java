/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import java.sql.Connection;

/**
 *
 * @author raymoon
 */
public interface ConnectionProvider {
    public Connection aquireConnection();
    
    public void releaseConnection();
}
