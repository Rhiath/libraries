/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.catalogentrystorage.persistence;

/**
 *
 * @author raymoon
 */
public class BusinessEntity {

    private String id;
    private String type;

    public BusinessEntity(String id) {
        this.id = id;
        this.type = this.getClass().getCanonicalName();
    }

    public BusinessEntity() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
