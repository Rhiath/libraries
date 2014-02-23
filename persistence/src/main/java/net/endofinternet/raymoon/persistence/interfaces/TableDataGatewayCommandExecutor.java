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
public interface TableDataGatewayCommandExecutor {

    /**
     * executes a command
     * 
     * @param commandToExecute the command to execute
     * @throws CommandExecutionFailedException if the command could not be fully executed / commit of the results failed
     */
    public void executeCommand(TableDateGatewayCommand commandToExecute) throws CommandExecutionFailedException;
}
