/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.queues.persistence;

import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public interface QueueTDG extends TableDataGateway {

    public <T> void enqueue(Class<T> type, T value) throws PersistenceException;

    public <T> QueueToken<T> peekAtNextQueueElement(Class<T> type) throws PersistenceException;

    public void dequeue(QueueToken token) throws PersistenceException;

    void createTableIfMissing() throws PersistenceException;
}
