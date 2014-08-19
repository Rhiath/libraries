/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.utilities;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;

/**
 *
 * @author raymoon
 */
public class Tables {

    public static boolean tableExists(SQLiteConnection connection, String tableName) throws PersistenceException {
        try {
            SQLiteStatement statement = connection.prepare("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = ?");
            try {
                statement.bind(1, tableName);
                statement.step();
                return (1 == statement.columnInt(0));
            } finally {
                statement.dispose();
            }
        } catch (SQLiteException ex) {
            throw new PersistenceException("failed to determine of table '" + tableName + "' exists", ex);
        }
    }
}
