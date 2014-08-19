package net.endofinternet.raymoon.persistence;

import com.almworks.sqlite4java.SQLiteConnection;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.implementation.AbstractTableDataGateway;
import net.endofinternet.raymoon.persistence.implementation.ConnectionProviderImpl;
import net.endofinternet.raymoon.persistence.implementation.SingleThreadedTableDataGatewayCommandExecutor;
import net.endofinternet.raymoon.persistence.implementation.TableDataGatewayLookupImpl;
import net.endofinternet.raymoon.persistence.interfaces.ConnectionProvider;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayLookup;
import net.endofinternet.raymoon.persistence.interfaces.TableDateGatewayCommand;
import net.endofinternet.raymoon.persistence.interfaces.exceptions.CommandExecutionFailedException;
import net.endofinternet.raymoon.persistence.utilities.Tables;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws CommandExecutionFailedException {

        ConnectionProvider connectionProvider = new ConnectionProviderImpl(new File("database.sqlite"));
        TableDataGatewayLookupImpl lookup = new TableDataGatewayLookupImpl();
        lookup.register(MyGateway.class, new MyGatewayImpl(connectionProvider));

        SingleThreadedTableDataGatewayCommandExecutor commandExecutor = new SingleThreadedTableDataGatewayCommandExecutor(lookup, connectionProvider);
        commandExecutor.executeCommand(new TableDateGatewayCommand() {
            @Override
            public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
                gatewayProvider.getGateway(MyGateway.class).setEntry(7);
            }
        });
    }

    /**
     *
     */
    public static class MyGatewayImpl extends AbstractTableDataGateway implements MyGateway {

        public MyGatewayImpl(ConnectionProvider connectionProvider) {
            super(connectionProvider);
        }

        @Override
        public void setEntry(int value) {
            try {
                SQLiteConnection connection = super.getConnectionProvider().aquireConnection();

                if ( Tables.tableExists(connection, "A") ){
                    System.out.println("table exists");
                } else {
                    System.out.println("table does not exist");
                };


            } catch ( PersistenceException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public int getEntry() {
            return 4;
        }
    }

    public static interface MyGateway extends TableDataGateway {

        public void setEntry(int value);

        public int getEntry();
    }
}
