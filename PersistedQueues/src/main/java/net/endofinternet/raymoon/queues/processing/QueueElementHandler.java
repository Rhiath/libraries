/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.queues.processing;

/**
 *
 * @author raymoon
 */
public interface QueueElementHandler<T> {

    public Class<T> getElementClassType();

    public void handlElement(T element);
}
