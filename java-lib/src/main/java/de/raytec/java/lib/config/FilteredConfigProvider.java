/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import de.raytec.java.lib.doumentation.SoftwarePattern;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author raymoon
 */
@SoftwarePattern(name = "Decorator", roles = "?")
public class FilteredConfigProvider implements ConfigProvider {

    private final List<String> visibleKeys = new LinkedList<String>();
    private final ConfigProvider unfilteredProvider;

    public FilteredConfigProvider(List<String> visibleKeys, ConfigProvider unfilteredProvider) {
        this.visibleKeys.addAll(visibleKeys);
        this.unfilteredProvider = unfilteredProvider;
    }

    public Config getConfigRoot() {
        return new FilteredConfig(visibleKeys, unfilteredProvider.getConfigRoot());
    }

    public Config getConfigByID(String id) {
        return new FilteredConfig(visibleKeys, unfilteredProvider.getConfigByID(id));
    }
}
