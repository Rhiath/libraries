/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.rocketScissorSpagegoo.obo;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Action;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Fleet;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.GameAI;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Planet;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.Player;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.ShipType;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.actions.AttackAction;
import net.endofinternet.raymoon.rocketscissorspacegoo.lib.actions.NullAction;

/**
 *
 * @author raymoon
 */
class GatlingAI implements GameAI {

    private final String name;

    public GatlingAI(String name) {
        this.name = name;
    }

    public Action computeAction(List<Player> players, List<Planet> planets, List<Fleet> fleets) {
        List<Planet> myPlanets = getMyPlanets(planets);
        List<Planet> otherPlanets = new LinkedList<Planet>(planets);
        otherPlanets.removeAll(myPlanets);

        List<AttackOption> options = new LinkedList<AttackOption>();
        for (Planet myPlanet : myPlanets) {
            for (Planet targetPlanet : otherPlanets) {
                options.add(new AttackOption(myPlanet, targetPlanet));
            }
        }

        Collections.sort(options, buildBestTargetComparator());
        if (options.isEmpty()) {
            return new NullAction();
        } else {
            Random r = new Random(System.currentTimeMillis());
            
            
            return buildAttackOrder(options.get(0));
        }
    }

    private List<Planet> getMyPlanets(List<Planet> planets) {
        List<Planet> retValue = new LinkedList<Planet>();

        for (Planet planet : planets) {
            if (planet.getOwner() != null) {
                if (name.equals(planet.getOwner().getName())) {
                    retValue.add(planet);
                }
            }
        }

        return retValue;
    }

    private Comparator<AttackOption> buildBestTargetComparator() {
        return new Comparator<AttackOption>() {
            public int compare(AttackOption c1, AttackOption c2) {
                double ratingC1 = rateAttackOrder(c1);
                double ratingC2 = rateAttackOrder(c2);

                if (ratingC1 > ratingC2) {
                    return -1;
                } else if (ratingC1 < ratingC2) {
                    return 1;
                } else {
                    return 0;
                }
            }

            private double rateAttackOrder(AttackOption c1) {
                double retValue = 0.0;

                retValue -= c1.getSource().getRequiredTravelTimeTo(c1.getDestination());
                retValue += getproductionRating(c1.getDestination().getProductionPerRound());
                retValue -= getproductionRating(c1.getDestination().getDefenseStrength());

              //  System.out.println(c1.getSource().getX()+"/"+c1.getSource().getY()+"   -->   "+c1.getDestination().getX()+"/"+c1.getDestination().getY()+"  :  "+retValue);
                
                return retValue;
            }

            private double getproductionRating(Map<ShipType, Integer> amount) {
                double rating = 0.0;
                for (ShipType type : ShipType.values()) {
                    rating += amount.get(type);
                }
                return rating;
            }
        };
    }

    private Action buildAttackOrder(AttackOption get) {
        return new AttackAction(get.getSource(), get.getDestination(), buildAttackStrength(get));
    }

    private Map<ShipType, Integer> buildAttackStrength(AttackOption option) {
        Map<ShipType, Integer> retValue = new HashMap<ShipType, Integer>();

        for (ShipType type : ShipType.values()) {
            retValue.put(type, option.getSource().getDefenseStrength().get(type) / 2);
        }

        return retValue;
    }
}
