/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.logging;

import de.raytec.java.lib.config.Config;
import org.apache.log4j.Logger;

/**
 *
 * @author raymoon
 */
public class Logging {
    
    public static void configure(Config config){
        // TODO
    }

    public static void debug(Class loggedIn, String message) {
        Logger.getLogger(loggedIn.getCanonicalName()).debug(message);
    }

    public static void debug(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn.getCanonicalName()).debug(message, exception);
    }

    public static void info(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn.getCanonicalName()).info(message, exception);
    }

    public static void info(Class loggedIn, String message) {
        Logger.getLogger(loggedIn.getCanonicalName()).info(message);
    }

    public static void warn(Class loggedIn, String message) {
        Logger.getLogger(loggedIn.getCanonicalName()).warn(message);
    }

    public static void warn(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn.getCanonicalName()).warn(message, exception);
    }

    public static void error(Class loggedIn, String message) {
        Logger.getLogger(loggedIn.getCanonicalName()).error(message);
    }

    public static void error(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn.getCanonicalName()).error(message, exception);
    }

    public static void fatal(Class loggedIn, String message) {
        Logger.getLogger(loggedIn.getCanonicalName()).fatal(message);
    }

    public static void fatal(Class loggedIn, String message, Exception exception) {
        Logger.getLogger(loggedIn.getCanonicalName()).fatal(message, exception);
    }
}
