/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.catalogentrystorage.persistence.impl;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import net.endofinternet.raymoon.catalogentrystorage.persistence.JsonEntityTDG;
import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.implementation.AbstractTableDataGateway;
import net.endofinternet.raymoon.persistence.interfaces.ConnectionProvider;
import net.endofinternet.raymoon.persistence.utilities.Tables;

/**
 *
 * @author raymoon
 */
public class JsonEntityTDGImpl extends AbstractTableDataGateway implements JsonEntityTDG {

    public JsonEntityTDGImpl(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public String getSerialisation(String id) throws PersistenceException {
        ConnectionProvider provider = super.getConnectionProvider();
        SQLiteConnection connection = provider.aquireConnection();
        try {
            SQLiteStatement statement = connection.prepare("select value from JSONENTITY WHERE id = ?").bind(1, id);
            try {
                while (statement.step()) {
                    return statement.columnString(0);
                }
                throw new PersistenceException("failed to get json entity");

            } finally {
                statement.dispose();
            }
        } catch (SQLiteException ex) {
            throw new PersistenceException("failed to get json entity", ex);
        } finally {
            provider.releaseConnection();
        }
    }

    @Override
    public void storeSerializsation(String id, String serialization) throws PersistenceException {
        ConnectionProvider provider = super.getConnectionProvider();
        SQLiteConnection connection = provider.aquireConnection();
        try {
            connection.prepare("delete from JSONENTITY where id = ?").bind(1, id).stepThrough();
            connection.prepare("insert  into JSONENTITY (id, value) VALUES (?,?)").bind(1, id).bind(2, serialization).stepThrough();
        } catch (SQLiteException ex) {
            throw new PersistenceException("failed to insert json entity '" + id + "'", ex);
        } finally {
            provider.releaseConnection();
        }
    }

    @Override
    public void createTableIfMissing() throws PersistenceException {
        ConnectionProvider provider = super.getConnectionProvider();
        SQLiteConnection connection = provider.aquireConnection();
        try {
            if (!Tables.tableExists(connection, "JSONENTITY")) {
                connection.prepare("create table JSONENTITY ("
                        + "id TEXT,"
                        + "value TEXT"
                        + ")").stepThrough();
            }
        } catch (SQLiteException ex) {
            throw new PersistenceException("failed to create tables for queues", ex);
        } finally {
            provider.releaseConnection();

        }
    }
}
