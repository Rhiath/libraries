/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces;

/**
 *
 * @author raymoon
 */
public interface Fetchable {

    /**
     * recursively retrieves the object implementing this interface from persistence.
     */
    public void fetch();
}
