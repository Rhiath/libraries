/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import java.util.HashMap;
import java.util.Map;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGatewayLookup;

/**
 *
 * @author raymoon
 */
public class TableDataGatewayLookupImpl implements TableDataGatewayLookup {

    private final Map<Class<? extends TableDataGateway>, TableDataGateway> lookup = new HashMap<Class<? extends TableDataGateway>, TableDataGateway>();

    /**
     * registers a table data gateway.
     *
     * @param <T> the table data gateway type that shall be registered
     * @param interfaceType the interface of the object to register
     * @param objectToRegister the object implementing the table data gateway
     * interface
     */
    public <T extends TableDataGateway> void register(Class<T> interfaceType, T objectToRegister) {
        lookup.put(interfaceType, objectToRegister);
    }

    /**
     * provides the decorated (fetching and logging) gateway to a database table
     *
     * @param <T> the table data gateway type that was requested
     * @param interfaceType the class object requested
     * @return
     */
    @Override
    public <T extends TableDataGateway> T getGateway(Class<T> interfaceType) {
        T retValue = (T) lookup.get(interfaceType);

        retValue = TableDataGatewayDecorator.addFetching(interfaceType, retValue);
        retValue = TableDataGatewayDecorator.addLogging(interfaceType, retValue);

        return retValue;
    }
}
