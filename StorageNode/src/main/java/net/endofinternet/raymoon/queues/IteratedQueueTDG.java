/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.queues;

import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public interface IteratedQueueTDG extends TableDataGateway {

    public <T> void createIterator(Class<T> type, String iteratorName) throws PersistenceException;

    public <T> void enqueue(Class<T> type, T value) throws PersistenceException;

    public <T> IteratedQueueToken<T> peekAtNextQueueElement(Class<T> type, String iteratorName) throws PersistenceException;

    public void dequeue(IteratedQueueToken token) throws PersistenceException;

    void createTableIfMissing() throws PersistenceException;
}
