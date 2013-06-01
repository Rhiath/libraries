/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketscissorspacegoo.lib;

import java.util.Comparator;
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

    public Planet(int x, int y, Player owner, Map<ShipType, Integer> defenseStrength,
            Map<ShipType, Integer> productionPerRound, int id) {
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

    public int getProductionDiversity() {
        int diversityLevel = 0;
        for (ShipType type : productionPerRound.keySet()) {
            if (productionPerRound.get(type) > 0) {
                diversityLevel++;
            }
        }
        return diversityLevel;
    }

    public int getSummedProductionPerRound() {
        int amount = 0;
        for (ShipType type : productionPerRound.keySet()) {
            amount += productionPerRound.get(type);
        }
        return amount;
    }

    public class PlanetComparator implements Comparator<Planet> {

        private SortParameter[] parameters;
        private Planet source;

        private PlanetComparator(SortParameter[] parameters, Planet source) {
            this.parameters = parameters;
            this.source = source;
        }

        public int compare(Planet p1, Planet p2) {
            for (SortParameter parameter : parameters) {
                switch (parameter) {
                case DISTANCE:
                    if (source.getRequiredTravelTimeTo(p1) > source.getRequiredTravelTimeTo(p2)) {
                        return 1;
                    } else if (source.getRequiredTravelTimeTo(p1) < source.getRequiredTravelTimeTo(p2)) {
                        return -1;
                    }
                    break;
                case PRODUCTION_DIVERSITY:
                    if (p1.getProductionDiversity() > p2.getProductionDiversity()) {
                        return 1;
                    } else if (p1.getProductionDiversity() < p2.getProductionDiversity()) {
                        return -1;
                    }
                    break;
                case PRODUCTION_PER_ROUND:
                    if (p1.getSummedProductionPerRound() > p2.getSummedProductionPerRound()) {
                        return 1;
                    } else if (p1.getSummedProductionPerRound() < p2.getSummedProductionPerRound()) {
                        return -1;
                    }
                    break;
                }
            }
            return 0;
        }
    }

    public enum SortParameter {
        DISTANCE, PRODUCTION_DIVERSITY, PRODUCTION_PER_ROUND
    }

    public Comparator<Planet> getComparator(SortParameter[] sortParameters, Planet sourcePlanet) {
        return new PlanetComparator(sortParameters, sourcePlanet);
    }
}
