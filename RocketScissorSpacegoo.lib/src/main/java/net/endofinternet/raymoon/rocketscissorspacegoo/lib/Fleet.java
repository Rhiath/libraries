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
public class Fleet {

    private final Player owner;
    private final Planet start;
    private final Planet destination;
    private final int travelStartRound;
    private final int travelEndRound;
    private final Map<ShipType, Integer> strength;

    public Fleet(Player owner, Planet start, Planet destination, int travelStartRound, int travelEndRound, Map<ShipType, Integer> strength) {
        this.owner = owner;
        this.start = start;
        this.destination = destination;
        this.travelStartRound = travelStartRound;
        this.travelEndRound = travelEndRound;
        this.strength = strength;
    }

    public Player getOwner() {
        return owner;
    }

    public Planet getStart() {
        return start;
    }

    public Planet getDestination() {
        return destination;
    }

    public int getTravelStartRound() {
        return travelStartRound;
    }

    public int getTravelEndRound() {
        return travelEndRound;
    }

    public Map<ShipType, Integer> getStrength() {
        return strength;
    }
}
