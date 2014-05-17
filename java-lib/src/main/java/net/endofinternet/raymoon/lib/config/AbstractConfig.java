/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.config;

import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.lib.Generic;
import net.endofinternet.raymoon.lib.documentation.SoftwarePattern;

/**
 *
 * @author raymoon
 */
 abstract class AbstractConfig implements Config {

    @SoftwarePattern(name = "Template Method", roles = "Abstract Class")
    public abstract String getString(String key) throws NoSuchKeyException, InvalidValueContentException;

    public final Boolean getBoolean(String key) throws NoSuchKeyException, InvalidValueContentException {
        @SoftwarePattern(name = "Template Method", roles = "Abstract Class")
        String value = getString(key);

        validateByRegex(key, value, getBooleanFormat());

        return Boolean.valueOf(value);
    }

    public final Long getLong(String key) throws NoSuchKeyException, InvalidValueContentException {
        @SoftwarePattern(name = "Template Method", roles = "Abstract Class")
        String value = getString(key);

        validateByRegex(key, value, getLongFormat());

        return Long.valueOf(value);
    }

    public final Double getDouble(String key) throws NoSuchKeyException, InvalidValueContentException {
        @SoftwarePattern(name = "Template Method", roles = "Abstract Class")
        String value = getString(key);

        validateByRegex(key, value, getDoubleFormat());

        return Double.valueOf(value);
    }

    public final List<String> getStrings(String key, char separator) throws NoSuchKeyException, InvalidValueContentException {
        @SoftwarePattern(name = "Template Method", roles = "Abstract Class")
        String value = getString(key);

        return Generic.asList(value.split("\\" + key));
    }

    public final List<Long> getLongs(String key, char separator) throws NoSuchKeyException, InvalidValueContentException {
        List<String> values = getStrings(key, separator);
        List<Long> retValue = new LinkedList<Long>();

        for (String value : values) {
            validateByRegex(key, value, getLongFormat());
            retValue.add(Long.valueOf(value));
        }

        return retValue;
    }

    public final List<Double> getDoubles(String key, char separator) throws NoSuchKeyException, InvalidValueContentException {
        List<String> values = getStrings(key, separator);
        List<Double> retValue = new LinkedList<Double>();

        for (String value : values) {
            validateByRegex(key, value, getDoubleFormat());
            retValue.add(Double.valueOf(value));
        }

        return retValue;
    }

    public final List<String> getKeysWithPrefix(String prefix) {
        List<String> retValue = new LinkedList<String>();

        for (String key : getAllKeys()) {
            if (key.startsWith(prefix)) {
                retValue.add(key);
            }
        }

        return retValue;
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
