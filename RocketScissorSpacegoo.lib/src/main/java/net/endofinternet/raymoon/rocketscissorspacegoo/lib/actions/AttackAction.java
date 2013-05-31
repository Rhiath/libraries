/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib.actions;

import java.util.Map;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Action;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Planet;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.ShipType;

/**
 *
 * @author raymoon
 */
public class AttackAction extends Action {

    private final Planet source;
    private final Planet destination;
    private final Map<ShipType, Integer> defenseStrength;

    public AttackAction(Planet source, Planet destination, Map<ShipType, Integer> defenseStrength) {
        super();
        this.source = source;
        this.destination = destination;
        this.defenseStrength = defenseStrength;
    }

    public Planet getSource() {
        return source;
    }

    public Planet getDestination() {
        return destination;
    }

    public Map<ShipType, Integer> getDefenseStrength() {
        return defenseStrength;
    }
}
