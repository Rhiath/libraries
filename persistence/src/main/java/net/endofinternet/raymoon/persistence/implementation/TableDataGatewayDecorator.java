/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public class TableDataGatewayDecorator {
    public static <T extends TableDataGateway> T addLogging(Class<T> interfaceType, T toDecorate){
        throw new UnsupportedOperationException("not yet implemented");
    }
    public static <T extends TableDataGateway> T addFetching(Class<T> interfaceType, T toDecorate){
        throw new UnsupportedOperationException("not yet implemented");
    }
}
