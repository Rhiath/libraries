/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib;

/**
 *
 * @author raymoon
 */
public class Account {

    private final String username;
    private final String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
