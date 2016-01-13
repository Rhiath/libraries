/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.resttest.restServices;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author raymoon
 */
public class RegistryImpl implements Registry {

    private final Map<Class, Object> services = new HashMap<>();

    @Override
    public <T> void register(Class<T> type, T service) {
        services.put(type, service);
    }

    @Override
    public List<Class> getRegisteredTypes() {
        return new LinkedList<>(services.keySet());
    }

    @Override
    public Object getService(Class type) {
        return services.get(type);
    }

}
