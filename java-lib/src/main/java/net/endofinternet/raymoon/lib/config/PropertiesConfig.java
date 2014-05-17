/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.config;

import java.util.List;
import java.util.Properties;
import net.endofinternet.raymoon.lib.Generic;
import net.endofinternet.raymoon.lib.documentation.SoftwarePattern;

/**
 *
 * @author raymoon
 */
@SoftwarePattern(name = "Object Adapter", roles = "Adapter")
class PropertiesConfig extends AbstractConfig {

    private final String identification;
    
    @SoftwarePattern(name = "Object Adapter", roles = "Adaptee")
    private final Properties properties;

    public PropertiesConfig(String identification, Properties properties) {
        this.identification = identification;
        this.properties = properties;
    }

    public String getIdentification() {
        return identification;
    }

    public String getString(String key) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);

        return properties.getProperty(key);
    }

    public List<String> getAllKeys() {
        return Generic.asList(properties.stringPropertyNames());
    }

    private void validateKeyHasValue(String key) throws NoSuchKeyException, InvalidValueContentException {
        if (!properties.containsKey(key)) {
            throw new NoSuchKeyException("the key '" + key + "' is not defined in config '" + this.getIdentification() + "'");
        }

        if (properties.getProperty(key) == null) {
            throw new InvalidValueContentException("the key '" + key + "' does not have a value in config '" + this.getIdentification() + "'");
        }
    }
}
