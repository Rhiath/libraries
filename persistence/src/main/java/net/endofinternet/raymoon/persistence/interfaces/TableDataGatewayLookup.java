/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces;

/**
 *
 * @author raymoon
 */
public interface TableDataGatewayLookup {

    /**
     * provides a specific table data gateway that has been registered to the
     * lookup
     *
     * @param <T> the table data gateway type that shall be provided
     * @param interfaceType the interface of the object to register
     * @return the gateway implementing the interface, or null if no such exists
     */
    public <T extends TableDataGateway> T getGateway(Class<T> interfaceType);
}
