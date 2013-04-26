/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import de.raytec.java.lib.Generic;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author raymoon
 */
class PropertiesConfig implements Config {

    private final String identification;
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

    public Boolean getBoolean(String key) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);

        String value = properties.getProperty(key);

        validateByRegex(key, value, getBooleanFormat());

        return Boolean.valueOf(value);
    }

    public Long getLong(String key) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);

        String value = properties.getProperty(key);

        validateByRegex(key, value, getLongFormat());

        return Long.valueOf(value);
    }

    public Double getDouble(String key) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);

        String value = properties.getProperty(key);

        validateByRegex(key, value, getDoubleFormat());

        return Double.valueOf(value);
    }

    public List<String> getStrings(String key, char separator) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);

        String value = properties.getProperty(key);

        return Generic.asList(value.split("\\" + key));
    }

    public List<Long> getLongs(String key, char separator) throws NoSuchKeyException, InvalidValueContentException {
        List<String> values = getStrings(key, separator);
        List<Long> retValue = new LinkedList<Long>();
        
        for ( String value : values ){
            validateByRegex(key, value, getLongFormat());
            retValue.add(Long.valueOf(value));
        }
        
        return retValue;
    }

    public List<Double> getDoubles(String key, char separator) throws NoSuchKeyException, InvalidValueContentException {
        List<String> values = getStrings(key, separator);
        List<Double> retValue = new LinkedList<Double>();
        
        for ( String value : values ){
            validateByRegex(key, value, getDoubleFormat());
            retValue.add(Double.valueOf(value));
        }
        
        return retValue;
    }

    public List<String> getAllKeys() {
        return Generic.asList(properties.stringPropertyNames());
    }

    public List<String> getKeysWithPrefix(String prefix) {
        List<String> retValue = new LinkedList<String>();
        
        for ( String key : getAllKeys()){
            if ( key.startsWith(prefix)){
                retValue.add(key);
            }
        }
        
        return retValue;
    }

    private void validateKeyHasValue(String key) throws NoSuchKeyException, InvalidValueContentException {
        if (!properties.containsKey(key)) {
            throw new NoSuchKeyException("the key '" + key + "' is not defined in config '" + this.getIdentification() + "'");
        }

        if (properties.getProperty(key) == null) {
            throw new InvalidValueContentException("the key '" + key + "' does not have a value in config '" + this.getIdentification() + "'");
        }
    }

    private void validateByRegex(String key, String value, String regexp) throws InvalidValueContentException {
        if (!value.matches(regexp)) {
            throw new InvalidValueContentException("the key '" + key + "' does not match the required format");
        }
    }

    private String getLongFormat() {
        return "\\A[0-9]+\\z";
    }

    private String getDoubleFormat() {
        return "\\A[0-9]+(\\.[0-9]+)?(E([+-][0-9]+))\\z";
    }

    private String getBooleanFormat() {
        return "\\A(true)|(false)\\z";
    }
}
