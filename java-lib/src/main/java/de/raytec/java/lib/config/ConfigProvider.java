/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.java.lib.config;

/**
 *
 * @author raymoon
 */
public interface ConfigProvider {
    public Config getConfigRoot();
    public Config getConfigByID(String id);
}
