/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public class AbstractTableDataGateway implements TableDataGateway {

    private final ConnectionProvider connectionProvider;

    public AbstractTableDataGateway(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }
}
