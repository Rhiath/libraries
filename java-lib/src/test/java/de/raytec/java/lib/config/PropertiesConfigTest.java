/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author raymoon
 */
public class PropertiesConfigTest {

    @Test
    public void testBooleanSyntax() {
        Properties p = new Properties();
        p.put("a", "truea");
        p.put("b", "true");
        p.put("c", "tru");

        Config config = new PropertiesConfig("someID", p);
        try {
            config.getBoolean("a");
            Assert.fail();
        } catch (NoSuchKeyException ex) {
            Assert.fail();
        } catch (InvalidValueContentException ex) {
            // right reponse
        }
        try {
            config.getBoolean("a");
            Assert.fail();
        } catch (NoSuchKeyException ex) {
            Assert.fail();
        } catch (InvalidValueContentException ex) {
            // right reponse
        }
        try {
            config.getBoolean("b");
            // right response
        } catch (NoSuchKeyException ex) {
            Assert.fail();
        } catch (InvalidValueContentException ex) {
            Assert.fail();
        }
        try {
            config.getBoolean("c");
            Assert.fail();
        } catch (NoSuchKeyException ex) {
            Assert.fail();
        } catch (InvalidValueContentException ex) {
            // right reponse
        }
        try {
            config.getBoolean("d");
            Assert.fail();
        } catch (NoSuchKeyException ex) {
            // right reponse
        } catch (InvalidValueContentException ex) {
            Assert.fail();
        }
    }

    @Test
    public void testInvalidLongSyntax() {
        Properties p = new Properties();
        p.put("a", "1356.23");
        p.put("b", "1356.0");
        p.put("c", "");
        p.put("d", "132E-23");
        p.put("e", "132E+1.2");
        p.put("f", "1356a");
        p.put("g", "1356E+12");
        p.put("h", "1356E0");

        Config config = new PropertiesConfig("someID", p);
        for (String key : config.getAllKeys()) {
            try {
                config.getLong(key);
                Assert.fail();
            } catch (NoSuchKeyException ex) {
                Assert.fail();
            } catch (InvalidValueContentException ex) {
                // right reponse
            }
        }
    }
    @Test
    public void testValidLongSyntax() {
        Properties p = new Properties();
        p.put("a", "1356");

        Config config = new PropertiesConfig("someID", p);
        for (String key : config.getAllKeys()) {
            try {
                config.getLong(key);
                // right reponse
            } catch (NoSuchKeyException ex) {
                Assert.fail();
            } catch (InvalidValueContentException ex) {
                Assert.fail();
            }
        }
    }
}
