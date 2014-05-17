/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.config;

import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.lib.documentation.SoftwarePattern;

/**
 *
 * @author raymoon
 */
@SoftwarePattern(name = "Decorator", roles = "?")
public class FilteredConfig extends AbstractConfig {

    private final List<String> visibleKeys = new LinkedList<String>();
    private final Config unfilteredConfig;

    public FilteredConfig(List<String> visibleKeys, Config unfilteredConfig) {
        this.visibleKeys.addAll(visibleKeys);
        this.unfilteredConfig = unfilteredConfig;
    }

    public String getIdentification() {
        return unfilteredConfig.getIdentification();
    }

    public String getString(String key) throws NoSuchKeyException, InvalidValueContentException {
        if (!getAllKeys().contains(key)) {
            throw new NoSuchKeyException("the key '" + key + "' is not defined in config '" + this.getIdentification() + "'");
        }

        return unfilteredConfig.getString(key);
    }

    public List<String> getAllKeys() {
        List<String> retValue = new LinkedList<String>();
        retValue.addAll(unfilteredConfig.getAllKeys());
        retValue.retainAll(visibleKeys);

        return retValue;
    }
}
