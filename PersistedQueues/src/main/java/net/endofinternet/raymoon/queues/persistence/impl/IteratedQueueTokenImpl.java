/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.queues.persistence.impl;

import net.endofinternet.raymoon.queues.persistence.QueueToken;

/**
 *
 * @author raymoon
 */
class IteratedQueueTokenImpl<T> implements QueueToken<T> {

    private T value;
    private long id;
    private String iteratorName;
    private String queueType;

    public IteratedQueueTokenImpl(T value, long id, String iteratorName, String queueType) {
        this.value = value;
        this.id = id;
        this.iteratorName = iteratorName;
        this.queueType = queueType;
    }

    @Override
    public T getValue() {
        return value;
    }

    public long getId() {
        return id;
    }

    public String getIteratorName() {
        return iteratorName;
    }

    public String getQueueType() {
        return queueType;
    }
}
