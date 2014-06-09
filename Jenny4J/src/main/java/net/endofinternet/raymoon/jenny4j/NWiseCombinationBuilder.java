/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.Collection;
import static java.util.Collections.list;
import java.util.Comparator;
import net.endofinternet.raymoon.utils.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Ray
 */
public class NWiseCombinationBuilder {

    private final List<Domain> domains;
    private final int combinationSize;

    public NWiseCombinationBuilder(List<Domain> domains, int combinationSize) {
        this.domains = domains;
        this.combinationSize = combinationSize;
    }

    public List<Combination> getCombinations() {
        Map<Integer, List<Combination>> combinationsForSize = new HashMap<Integer, List<Combination>>();
        for (int i = 1; i <= combinationSize; i++) {
            combinationsForSize.put(i, new LinkedList<Combination>());
        }
        System.out.println("building combinations of length 1/" + combinationSize);
        combinationsForSize.put(1, buildUnaryCombinations());
        Map<Integer, List<Combination>> unaryCombinationsForPosition = buildUnaryCombinationsPerPositionMap(combinationsForSize.get(1));
        System.out.println("built " + combinationsForSize.get(1).size() + " combinations");

        for (int i = 2; i <= combinationSize; i++) { // for all missing sizes
            System.out.println("building combinations of length " + i + "/" + combinationSize);
            for (Combination lastIteration : combinationsForSize.get(i - 1)) {
                Collection<Integer> unmappedPositions = getUnmappedPositions(lastIteration);
                for (Integer unmappedPosition : unmappedPositions) {
                    for (Combination unaryValues : unaryCombinationsForPosition.get(unmappedPosition)) {
                        combinationsForSize.get(i).add(CombinationUtils.combine(unaryValues, lastIteration));
                    }
                }
            }
            System.out.println("found " + combinationsForSize.get(i).size() + " combinations (with duplicates)");
            combinationsForSize.put(i, removeDuplicates(combinationsForSize.get(i)));
            System.out.println("built " + combinationsForSize.get(i).size() + " combinations");
        }

        return new LinkedList<Combination>(combinationsForSize.get(combinationSize));
    }

    private List<Combination> buildUnaryCombinations() {
        List<Combination> retValue = new LinkedList<Combination>();

        for (Domain domain : domains) {
            for (int value = 0; value < domain.getNumberOfPossibleValues(); value++) {
                retValue.add(new Combination(Collections.asSet(new ValueForPosition(domain.getPosition(), value))));
            }
        }

        return retValue;
    }

    private Map<Integer, List<Combination>> buildUnaryCombinationsPerPositionMap(Collection<Combination> unaryCombinations) {
        Map<Integer, List<Combination>> retValue = new HashMap<Integer, List<Combination>>();

        for (Combination toMap : unaryCombinations) {
            Integer position = getPositionForUnaryCombination(toMap);

            if (!retValue.containsKey(position)) {
                retValue.put(position, new LinkedList<Combination>());
            }

            retValue.get(position).add(toMap);
        }

        return retValue;
    }

    private Integer getPositionForUnaryCombination(Combination toMap) {
        return toMap.getValues().iterator().next().getPosition();
    }

    private Set<Integer> getUnmappedPositions(Combination lastIteration) {
        Set<Integer> retValue = new HashSet<Integer>();

        for (Domain domain : domains) {
            retValue.add(domain.getPosition());
        }

        for (ValueForPosition vfp : lastIteration.getValues()) {
            retValue.remove(vfp.getPosition());
        }

        return retValue;
    }

    private List<Combination> removeDuplicates(List<Combination> list) {
        Set setItems = new LinkedHashSet(list);

        return new LinkedList<Combination>(setItems);
    }
}
