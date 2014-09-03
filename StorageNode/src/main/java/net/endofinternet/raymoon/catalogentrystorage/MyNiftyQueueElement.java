/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.catalogentrystorage;

import net.endofinternet.raymoon.catalogentrystorage.persistence.BusinessEntity;

/**
 *
 * @author raymoon
 */
public class MyNiftyQueueElement extends BusinessEntity {

    private String d1;
    private int d2;

    public MyNiftyQueueElement() {
    }

    public MyNiftyQueueElement(String id, String d1, int d2) {
        super(id);
        this.d1 = d1;
        this.d2 = d2;
    }

    public String getD1() {
        return d1;
    }

    public int getD2() {
        return d2;
    }
}
