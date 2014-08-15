/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces;

import com.almworks.sqlite4java.SQLiteConnection;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author raymoon
 */
public interface ConnectionProvider {

    public SQLiteConnection aquireConnection() throws SQLException;

    public void releaseConnection() throws SQLException;
}
