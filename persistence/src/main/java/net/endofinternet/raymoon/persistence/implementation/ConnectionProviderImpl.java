/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

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
    private Connection connection;

    @Override
    public synchronized Connection aquireConnection() throws SQLException {
        if (referenceCount == 0) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            } catch (ClassNotFoundException ex) {
                throw new SQLException("failed to aquite SQL connection", ex);
            }
        }

        referenceCount++;
        return connection;

    }

    @Override
    public synchronized void releaseConnection() throws SQLException {

        referenceCount--;
        if (referenceCount == 0) {
            connection.close();
        }
    }
}
