/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces;

import net.endofinternet.raymoon.persistence.interfaces.exceptions.CommandExecutionFailedException;

/**
 *
 * @author raymoon
 */
public interface TableDateGatewayCommand {

    /**
     * executes a command on the database
     * 
     * @param gatewayProvider the lookup to the individual gateways
     * @throws CommandExecutionFailedException if the command could not be fully executed / commit of the results failed
     */
    public void executeCommand(TableDataGatewayLookup gatewayProvider) throws CommandExecutionFailedException;
}
