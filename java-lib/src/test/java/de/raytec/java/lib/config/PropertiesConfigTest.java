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
}
