/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

import de.raytec.java.lib.Generic;

/**
 *
 * @author raymoon
 */
public class ConfigComparison {
    public static boolean configurationsAreEqual(Config configA, Config configB) throws NoSuchKeyException, InvalidValueContentException{
        boolean retValue = true;
        
        retValue = configA.getAllKeys().equals(configB.getAllKeys());
        
        if  ( retValue ){
            for ( String key : configA.getAllKeys()){
                retValue &= Generic.equals(configA.getString(key), configB.getString(key));
            }
        }
        
        return retValue;
    }
}
