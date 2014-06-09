/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.endofinternet.raymoon.utils.Collections;

/**
 *
 * @author Ray
 */
public class BruteForceLookupBuilder {

    public static Map<Combination, List<Combination>> buildBruteForceLookup(List<Combination> bruteForce, List<Combination> uncoveredCombinations, int domainSize) {
        Map<Combination, List<Combination>> retValue = new HashMap<Combination, List<Combination>>();

        Map<Combination, List<Combination>> bruteForceWithValue = new HashMap<Combination, List<Combination>>();
        int processedBruteForceCount = 0;
        int lastPercent = 0;
        for (Combination bruteForceInstance : bruteForce) {
            List<Combination> npairCandidates = buildNPairCandidates(bruteForceInstance, domainSize);

            for (Combination npairCandidate : npairCandidates) {

                if (!bruteForceWithValue.containsKey(npairCandidate)) {
                    bruteForceWithValue.put(npairCandidate, new LinkedList<Combination>());
                }
                bruteForceWithValue.get(npairCandidate).add(bruteForceInstance);
            }
            processedBruteForceCount++;

            int percent = (processedBruteForceCount * 100) / (bruteForce.size());
            if (percent - lastPercent >= 5) {
                System.out.println(percent + " % of brute force lookup is done");
                lastPercent = percent;
            }
        }

        int coveredCount = 0;
        for (Combination uncovered : uncoveredCombinations) {
            retValue.put(uncovered, bruteForceWithValue.get(uncovered));

            coveredCount++;
//            System.out.println(coveredCount + "/" + uncoveredCombinations.size());
        }

        System.out.println("built brute force lookup per " + domainSize + " value(s)");

        return retValue;
    }

    public static List<Combination> buildNPairCandidates(Combination bruteForceInstance, int domainSize) {
        List<Combination> retValue = new LinkedList<Combination>();

        List<ValueForPosition> values = new LinkedList<ValueForPosition>(bruteForceInstance.getValues());

        recurse(retValue, values, new LinkedList<ValueForPosition>(), domainSize);

        return retValue;
    }

    private static void recurse(List<Combination> retValue, List<ValueForPosition> remainingValues, List<ValueForPosition> chosenValues, int domainSize) {
        if (domainSize == 0) {
            retValue.add(new Combination(new HashSet<ValueForPosition>(chosenValues)));
        } else {
            for (int i = 0; i < remainingValues.size(); i++) {
                List<ValueForPosition> remainingValuesSublist = new LinkedList<ValueForPosition>();
                for (int j = i + 1; j < remainingValues.size(); j++) {
                    remainingValuesSublist.add(remainingValues.get(j));
                }
                List<ValueForPosition> chosenValuesSublist = new LinkedList<ValueForPosition>(chosenValues);
                chosenValuesSublist.add(remainingValues.get(i));
                recurse(retValue, remainingValuesSublist, chosenValuesSublist, domainSize - 1);
            }
        }
    }

}
