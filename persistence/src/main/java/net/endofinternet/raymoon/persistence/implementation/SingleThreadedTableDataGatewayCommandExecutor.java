/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import net.endofinternet.raymoon.persistence.interfaces.ConnectionProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Override
    public synchronized void executeCommand(TableDateGatewayCommand commandToExecute) throws CommandExecutionFailedException {

        try {
            SQLiteConnection connection = connectionProvider.aquireConnection();
            connection.open(true);

            try {
                connection.exec("BEGIN");
                commandToExecute.executeCommand(lookup);
                connection.exec("COMMIT");
            } catch (CommandExecutionFailedException ex) {
                connection.exec("ROLLBACK");
                throw ex;
            } catch (Exception ex) {
                connection.exec("ROLLBACK");
                throw new CommandExecutionFailedException("failed to execute command", ex);
            } finally {
                connection.dispose();
            }
        } catch (SQLException | SQLiteException ex) {
            throw new CommandExecutionFailedException("failed to execute command", ex);
        }
    }
}
