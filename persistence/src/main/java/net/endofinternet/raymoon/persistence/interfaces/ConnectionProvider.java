/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces;

import com.almworks.sqlite4java.SQLiteConnection;
import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;

/**
 *
 * @author raymoon
 */
public interface ConnectionProvider {

    public SQLiteConnection aquireConnection() throws PersistenceException;

    public void releaseConnection() throws PersistenceException;
}
