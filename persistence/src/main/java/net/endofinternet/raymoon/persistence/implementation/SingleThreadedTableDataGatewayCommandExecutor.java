/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayCommandExecutor;
import net.endofinternet.raymoon.persistence.interfaces.TableDateGatewayCommand;
import net.endofinternet.raymoon.persistence.interfaces.exceptions.CommandExecutionFailedException;

/**
 *
 * @author raymoon
 */
public class SingleThreadedTableDataGatewayCommandExecutor implements TableDataGatewayCommandExecutor {

    private final EntityManager entityManager;
    private final TableDataGatewayLookupImpl lookup;

    public SingleThreadedTableDataGatewayCommandExecutor(TableDataGatewayLookupImpl lookup) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPA");
        entityManager = factory.createEntityManager();
        this.lookup = lookup;
        
        lookup.setEntityManager(entityManager);
    }

    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    public void commitTransaction() {
        entityManager.getTransaction().commit();
    }

    public void rollbackTransaction() {
        entityManager.getTransaction().rollback();
    }

    public synchronized void executeCommand(TableDateGatewayCommand commandToExecute) throws CommandExecutionFailedException {

        beginTransaction();
        try {
            commandToExecute.executeCommand(lookup);
            commitTransaction();
        } catch (CommandExecutionFailedException ex) {
            rollbackTransaction();
            throw ex;
        } catch (Exception ex) {
            rollbackTransaction();
            throw new CommandExecutionFailedException("failed to execute command", ex);
        }
    }
}
