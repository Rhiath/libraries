/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import com.almworks.sqlite4java.SQLite;
import com.almworks.sqlite4java.SQLiteConnection;
import java.io.File;
import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.interfaces.ConnectionProvider;

/**
 *
 * @author raymoon
 */
public class ConnectionProviderImpl implements ConnectionProvider {
    
    static {
         SQLite.setLibraryPath("target/lib");
    }

    private int referenceCount = 0;
    private SQLiteConnection connection;
    private final File storageLocation;

    public ConnectionProviderImpl(File storageLocation) {
       this.storageLocation = storageLocation;
    }

    @Override
    public synchronized SQLiteConnection aquireConnection() throws PersistenceException {
        if (referenceCount == 0) {
            connection = new SQLiteConnection(storageLocation);
        }

        referenceCount++;
        return connection;

    }

    @Override
    public synchronized void releaseConnection() throws PersistenceException {

        referenceCount--;
        if (referenceCount == 0) {
            connection.dispose();
        }
    }
}
