/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import de.raytec.java.lib.Generic;
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
        
        validateByRegex(key, value, "\\A(true)|(false)\\z"); //TODO
        
        return Boolean.valueOf(value);
    }

    public Long getLong(String key) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);
        
        String value = properties.getProperty(key);
        
        validateByRegex(key, value, "\\A[0-9]+\\z"); //TODO
        
        return Long.valueOf(value);
    }

    public Double getDouble(String key) throws NoSuchKeyException, InvalidValueContentException {
        validateKeyHasValue(key);
        
        String value = properties.getProperty(key);
        
        validateByRegex(key, value, "\\A[0-9]+(\\.[0-9]+)?(E([+-][0-9]+))\\z"); //TODO
        
        return Double.valueOf(value);
    }

    public List<String> getStrings(String key, String separator) throws NoSuchKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Long> getLongs(String key, String separator) throws NoSuchKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Double> getDoubles(String key, String separator) throws NoSuchKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getAllKeys() {
       return Generic.asList(properties.stringPropertyNames());
    }

    public List<String> getKeysWithPrefix(String prefix) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void validateKeyHasValue(String key) throws NoSuchKeyException, InvalidValueContentException {
        if (!properties.containsKey(key)){
            throw new NoSuchKeyException("the key '"+key+"' is not defined in config '"+this.getIdentification()+"'");
        }
        
        if( properties.getProperty(key) == null ){
            throw new InvalidValueContentException("the key '"+key+"' does not have a value in config '"+this.getIdentification()+"'");
        }
    }

    private void validateByRegex(String key, String value, String regexp) throws InvalidValueContentException {
        if ( !value.matches(regexp)){
            throw new InvalidValueContentException("the key '"+key+"' does not match the required format");
        }
    }
    
}
