/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import java.util.List;

/**
 *
 * @author raymoon
 */
public interface Config {
    
    public String getIdentification();

    public String getString(String key) throws NoSuchKeyException, InvalidValueContentException;

    public Boolean getBoolean(String key) throws NoSuchKeyException, InvalidValueContentException;

    public Long getLong(String key) throws NoSuchKeyException, InvalidValueContentException;

    public Double getDouble(String key) throws NoSuchKeyException, InvalidValueContentException;

    public List<String> getStrings(String key, String separator) throws NoSuchKeyException;

    public List<Long> getLongs(String key, String separator) throws NoSuchKeyException;

    public List<Double> getDoubles(String key, String separator) throws NoSuchKeyException;
    
    public List<String> getAllKeys();
    
    public List<String> getKeysWithPrefix(String prefix);
}
