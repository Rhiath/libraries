/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketScissorSpagegoo.obo;

import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Planet;

/**
 *
 * @author raymoon
 */
class AttackOption {

    private final Planet source;
    private final Planet destination;

    public AttackOption(Planet source, Planet destination) {
        this.source = source;
        this.destination = destination;
    }

    public Planet getSource() {
        return source;
    }

    public Planet getDestination() {
        return destination;
    }
}
