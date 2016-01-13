/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest.restServices;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface Registry {

    /**
     * registers a given service implementation by its class object. If multiple
     * instances are registered for the same class object, the last instance is
     * retained.
     *
     * @param <T> template type
     * @param type the class types of the rest service
     * @param service the implementing object of the service.
     */
    <T> void register(Class<T> type, T service);

    /**
     * retrieves the list of currently registered service types
     *
     * @return list of service types
     */
    List<Class> getRegisteredTypes();

    /**
     * retrieves the implementing object of the service type
     *
     * @param type the type of the service
     * @return the service implementation
     */
    Object getService(Class type);
}
