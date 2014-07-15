/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import java.sql.Connection;
import java.sql.SQLException;
import net.endofinternet.raymoon.lib.logging.Logging;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayCommandExecutor;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayLookup;
import net.endofinternet.raymoon.persistence.interfaces.TableDateGatewayCommand;
import net.endofinternet.raymoon.persistence.interfaces.exceptions.CommandExecutionFailedException;

/**
 *
 * @author raymoon
 */
public class SingleThreadedTableDataGatewayCommandExecutor implements TableDataGatewayCommandExecutor {

    private final TableDataGatewayLookup lookup;
    private final ConnectionProvider connectionProvider;

    public SingleThreadedTableDataGatewayCommandExecutor(TableDataGatewayLookup lookup, ConnectionProvider connectionProvider) {
        this.lookup = lookup;
        this.connectionProvider = connectionProvider;
    }

    public void beginTransaction(Connection connection) throws SQLException {
        connection.setTransactionIsolation(Connection.TRANSACTION_NONE);
    }

    public void commitTransaction(Connection connection) throws SQLException {
        connection.commit();
    }

    public void rollbackTransaction(Connection connection) throws SQLException {
        connection.rollback();
    }

    @Override
    public synchronized void executeCommand(TableDateGatewayCommand commandToExecute) throws CommandExecutionFailedException {

        Connection connection = connectionProvider.aquireConnection();
        try {
            beginTransaction(connection);
            commandToExecute.executeCommand(lookup);
            commitTransaction(connection);
        } catch (CommandExecutionFailedException ex) {
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex1) {
                Logging.warn(this.getClass(), "failed torollback transaction", ex1);
                throw new CommandExecutionFailedException("failed to roll back after error", ex);
            }
            throw ex;
        } catch (Exception ex) {
            try {
                rollbackTransaction(connection);
            } catch (SQLException ex1) {
                Logging.warn(this.getClass(), "failed torollback transaction", ex1);
                throw new CommandExecutionFailedException("failed to roll back after error", ex);
            }
            throw new CommandExecutionFailedException("failed to execute command", ex);
        } finally {
            connectionProvider.releaseConnection();
        }
    }
}
