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
import net.endofinternet.raymoon.persistedqueues.IteratedQueueTDG;
import net.endofinternet.raymoon.persistedqueues.QueueTDG;
import net.endofinternet.raymoon.persistedqueues.QueueToken;
import net.endofinternet.raymoon.persistedqueues.impl.IteratedQueueTDGImpl;
import net.endofinternet.raymoon.persistedqueues.impl.QueueTDGImpl;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws CommandExecutionFailedException {
        Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.OFF);
        ConnectionProvider connectionProvider = new ConnectionProviderImpl(new File("database.sqlite"));
        TableDataGatewayLookupImpl lookup = new TableDataGatewayLookupImpl();
        lookup.register(QueueTDG.class, new QueueTDGImpl(connectionProvider));
        lookup.register(IteratedQueueTDG.class, new IteratedQueueTDGImpl(connectionProvider));
        lookup.register(net.endofinternet.raymoon.persistence.App.MyGateway.class, new net.endofinternet.raymoon.persistence.App.MyGatewayImpl(connectionProvider));

        SingleThreadedTableDataGatewayCommandExecutor commandExecutor = new SingleThreadedTableDataGatewayCommandExecutor(lookup, connectionProvider);
//        commandExecutor.executeCommand(new TableDateGatewayCommand() {
//            @Override
//            public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
//                try {
//                    gatewayProvider.getGateway(QueueTDG.class).createTableIfMissing();
//
//                    gatewayProvider.getGateway(QueueTDG.class).enqueue(MyNiftyQueueElement.class, new MyNiftyQueueElement("hallo Welt", 5));
//                    QueueToken<MyNiftyQueueElement> token = gatewayProvider.getGateway(QueueTDG.class).peekAtNextQueueElement(MyNiftyQueueElement.class);
//                    System.out.println(token.getValue().getD1());
//                    System.out.println(token.getValue().getD2());
//                    gatewayProvider.getGateway(QueueTDG.class).dequeue(token);
//                } catch (PersistenceException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });

//        commandExecutor.executeCommand(new TableDateGatewayCommand() {
//            @Override
//            public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
//                try {
//                    gatewayProvider.getGateway(IteratedQueueTDG.class).createTableIfMissing();
//                    gatewayProvider.getGateway(IteratedQueueTDG.class).createIterator(MyNiftyQueueElement.class, "1");
//
//                    gatewayProvider.getGateway(IteratedQueueTDG.class).enqueue(MyNiftyQueueElement.class, new MyNiftyQueueElement("hallo Welt", 5));
//                } catch (PersistenceException ex) {
//                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
        commandExecutor.executeCommand(new TableDateGatewayCommand() {
            @Override
            public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
                try {
                    QueueToken<MyNiftyQueueElement> token = gatewayProvider.getGateway(IteratedQueueTDG.class).peekAtNextQueueElement(MyNiftyQueueElement.class, "2");
                    System.out.println(token.getValue().getD1());
                    System.out.println(token.getValue().getD2());
                    gatewayProvider.getGateway(IteratedQueueTDG.class).dequeue(token);
                } catch (PersistenceException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
//
//        for (int i = 0; i < 10000; i++) {
//            log(i);
//
//            commandExecutor.executeCommand(new TableDateGatewayCommand() {
//                @Override
//                public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
//                    try {
//                        gatewayProvider.getGateway(QueueTDG.class).enqueue(MyNiftyQueueElement.class, new MyNiftyQueueElement("hallo Welt", 5));
//                    } catch (PersistenceException ex) {
//                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            });
//
//        }


    }

    private static void log(int i) {
        if (i % 100 == 0) {
            System.out.println("stored " + i);
        }
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

                if (Tables.tableExists(connection, "A")) {
                    System.out.println("table exists");
                } else {
                    System.out.println("table does not exist");
                };


            } catch (PersistenceException ex) {
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
