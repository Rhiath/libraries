/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.implementation;

import javax.persistence.EntityManager;
import net.endofinternet.raymoon.persistence.interfaces.TableDataGateway;

/**
 *
 * @author raymoon
 */
public class AbstractTableDataGateway implements TableDataGateway {
    
    protected EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
}
