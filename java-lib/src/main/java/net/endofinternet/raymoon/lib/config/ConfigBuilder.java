/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.lib.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.endofinternet.raymoon.lib.logging.Logging;
import net.endofinternet.raymoon.lib.transmission.TransmissionException;

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
