/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import de.raytec.java.lib.logging.Logging;
import de.raytec.java.lib.transmission.TransmissionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raymoon
 */
public abstract  class ConfigBuilder {
 public static Config buildConfig(File file) throws TransmissionException {
     Properties properties = new Properties();
     
     InputStream is = null;
     try {
         is= new FileInputStream(file);
         properties.load(is);
         
         return new PropertiesConfig(file.getAbsolutePath(), properties);
     } catch (FileNotFoundException ex) {
         throw new TransmissionException("failed to build config object from file", ex);
     } catch (IOException ex) {
         throw new TransmissionException("failed to build config object from file", ex);
     } finally {
         if ( is != null ){
             try {
                 is.close();
             } catch (IOException ex) {
                 Logging.warn(ConfigBuilder.class, "failed to close file input stream to file '"+file+"'", ex);
             }
         }
     }
     
 }  
}
