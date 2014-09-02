/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistedqueues.impl;

import net.endofinternet.raymoon.persistedqueues.QueueToken;

/**
 *
 * @author raymoon
 */
class QueueTokenImpl<T> implements QueueToken<T> {

    private T value;
    private long id;

    public QueueTokenImpl(T value, long id) {
        this.value = value;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public T getValue() {
        return value;
    }
}
