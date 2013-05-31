/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib;

import java.util.Map;

/**
 *
 * @author raymoon
 */
public class Planet {

    private final int x;
    private final int y;
    private final Player owner;
    private final Map<ShipType, Integer> defenseStrength;
    private final Map<ShipType, Integer> productionPerRound;
    private final int id;

    public Planet(int x, int y, Player owner, Map<ShipType, Integer> defenseStrength, Map<ShipType, Integer> productionPerRound, int id) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.defenseStrength = defenseStrength;
        this.productionPerRound = productionPerRound;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getOwner() {
        return owner;
    }

    public Map<ShipType, Integer> getDefenseStrength() {
        return defenseStrength;
    }

    private int getRequiredTravelTimeTo(Planet destination) {
        return (int) Math.ceil(Math.sqrt(Math.pow(x - destination.x, 2.0) + Math.pow(y - destination.y, 2.0)));
    }
}
