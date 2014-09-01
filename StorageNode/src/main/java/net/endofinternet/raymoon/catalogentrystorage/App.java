package net.endofinternet.raymoon.catalogentrystorage;

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
import net.endofinternet.raymoon.queues.QueueTDG;
import net.endofinternet.raymoon.queues.QueueToken;
import net.endofinternet.raymoon.queues.impl.QueueTDGImpl;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args) throws CommandExecutionFailedException {

        ConnectionProvider connectionProvider = new ConnectionProviderImpl(new File("database.sqlite"));
        TableDataGatewayLookupImpl lookup = new TableDataGatewayLookupImpl();
        lookup.register(QueueTDG.class, new QueueTDGImpl(connectionProvider));
        lookup.register(net.endofinternet.raymoon.persistence.App.MyGateway.class, new net.endofinternet.raymoon.persistence.App.MyGatewayImpl(connectionProvider));

        SingleThreadedTableDataGatewayCommandExecutor commandExecutor = new SingleThreadedTableDataGatewayCommandExecutor(lookup, connectionProvider);
        commandExecutor.executeCommand(new TableDateGatewayCommand() {
            @Override
            public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
                try {
                    gatewayProvider.getGateway(QueueTDG.class).createTableIfMissing();
                    
                    gatewayProvider.getGateway(QueueTDG.class).enqueue(MyNiftyQueueElement.class, new MyNiftyQueueElement("hallo Welt",5));
                    QueueToken<MyNiftyQueueElement> token = gatewayProvider.getGateway(QueueTDG.class).peekAtNextQueueElement(MyNiftyQueueElement.class);
                    System.out.println(token.getValue().getD1());
                    System.out.println(token.getValue().getD2());
                    gatewayProvider.getGateway(QueueTDG.class).dequeue(token);
                } catch (PersistenceException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                Logger.getLogger(net.endofinternet.raymoon.persistence.App.class.getName()).log(Level.SEVERE, null, ex);
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
