/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.logging;

import net.endofinternet.raymoon.lib.config.Config;
import net.endofinternet.raymoon.lib.config.InvalidValueContentException;
import net.endofinternet.raymoon.lib.config.NoSuchKeyException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author raymoon
 */
public class Logging {
    
    public static void configure(Config config) throws NoSuchKeyException, InvalidValueContentException{
        Properties p = new Properties();
        for ( String key : config.getAllKeys()){
            p.put(key, config.getString(key));
        }
        PropertyConfigurator.configure(p);
    }

    public static void debug(Class loggedIn, String message) {
        Logger.getLogger(loggedIn).debug(message);
    }

    public static void debug(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn).debug(message, exception);
    }

    public static void info(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn).info(message, exception);
    }

    public static void info(Class loggedIn, String message) {
        Logger.getLogger(loggedIn).info(message);
    }

    public static void warn(Class loggedIn, String message) {
        Logger.getLogger(loggedIn).warn(message);
    }

    public static void warn(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn).warn(message, exception);
    }

    public static void error(Class loggedIn, String message) {
        Logger.getLogger(loggedIn).error(message);
    }

    public static void error(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn).error(message, exception);
    }

    public static void fatal(Class loggedIn, String message) {
        Logger.getLogger(loggedIn).fatal(message);
    }

    public static void fatal(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn).fatal(message, exception);
    }
}
