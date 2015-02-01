/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.queues.processing;

import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayCommandExecutor;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayLookup;
import net.endofinternet.raymoon.persistence.interfaces.TableDateGatewayCommand;
import net.endofinternet.raymoon.persistence.interfaces.exceptions.CommandExecutionFailedException;
import net.endofinternet.raymoon.queues.persistence.QueueTDG;

/**
 *
 * @author raymoon
 */
public class QueueProcessor<T> extends Thread {

    private boolean keepRunning = true;
    private final Object sync = new Object();
    private final QueueElementHandler<T> handler;
    private final TableDataGatewayCommandExecutor commandExecutor;

    public QueueProcessor(QueueElementHandler<T> handler, TableDataGatewayCommandExecutor commandExecutor) {
        this.handler = handler;
        this.commandExecutor = commandExecutor;
    }

    public void dispose() {
        keepRunning = false;
        synchronized (sync) {
            sync.notify();
        }
    }

    public void newQueueElementAvailable() {
        synchronized (sync) {
            sync.notify();
        }
    }

    @Override
    public void run() {
        while (keepRunning) {

            handleAvailableElements();

            synchronized (sync) {
                try {
                    sync.wait(1000);
                } catch (InterruptedException ex) {
                    // ignore
                }
            }
        }
    }

    private void handleAvailableElements() {
        boolean checkForMoreElements = true;

        while (checkForMoreElements && keepRunning) {
            checkForMoreElements = handleNextElement();
        }
    }

    private boolean handleNextElement() {
        RetrieveNextQueueElement<T> command = new RetrieveNextQueueElement<T>();

        try {
            commandExecutor.executeCommand(command);

            handler.handlElement(command.retValue);
            return true;
        } catch (CommandExecutionFailedException ex) {
            return false;
        }
    }

    private class RetrieveNextQueueElement<T> implements TableDateGatewayCommand {

        private T retValue;

        public RetrieveNextQueueElement() {
        }

        public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException {
            try {
                retValue = (T) gatewayProvider.getGateway(QueueTDG.class).peekAtNextQueueElement(handler.getElementClassType());
            } catch (PersistenceException ex) {
                throw new CommandExecutionFailedException("failed to retrieve next queue element", ex);
            }
        }
    }
}
