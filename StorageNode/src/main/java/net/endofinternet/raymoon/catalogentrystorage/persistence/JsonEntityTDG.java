/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.catalogentrystorage.persistence;

import net.endofinternet.raymoon.persistence.exceptions.PersistenceException;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public interface JsonEntityTDG extends TableDataGateway {

    public String getSerialisation(String id) throws PersistenceException;

    public void storeSerializsation(String id, String serialization) throws PersistenceException;
    
    public void createTableIfMissing() throws PersistenceException;
}
