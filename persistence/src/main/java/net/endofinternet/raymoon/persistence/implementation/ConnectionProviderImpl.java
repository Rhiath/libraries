/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import com.almworks.sqlite4java.SQLiteConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.endofinternet.raymoon.persistence.interfaces.ConnectionProvider;

/**
 *
 * @author raymoon
 */
public class ConnectionProviderImpl implements ConnectionProvider {

    private int referenceCount = 0;
    private SQLiteConnection connection;

    @Override
    public synchronized SQLiteConnection aquireConnection() throws SQLException {
        if (referenceCount == 0) {
            connection = new SQLiteConnection(new File("/tmp/database"));
        }

        referenceCount++;
        return connection;

    }

    @Override
    public synchronized void releaseConnection() throws SQLException {

        referenceCount--;
        if (referenceCount == 0) {
            connection.dispose();
        }
    }
}
