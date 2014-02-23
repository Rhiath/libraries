/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.persistence.interfaces;

import javax.persistence.EntityManager;

/**
 *
 * @author raymoon
 */
public interface TableDataGateway {

    public void setEntityManager(EntityManager entityManager);
    
}
