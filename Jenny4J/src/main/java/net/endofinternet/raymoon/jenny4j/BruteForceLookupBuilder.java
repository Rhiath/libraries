/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ray
 */
public class BruteForceLookupBuilder {

    public static Map<Combination, List<Combination>> buildBruteForceLookup(List<Combination> bruteForce, List<Combination> uncoveredCombinations) {
        Map<Combination, List<Combination>> retValue = new HashMap<Combination, List<Combination>>();

        int covered = 0;
        for (Combination uncoveredCombination : uncoveredCombinations) {
            retValue.put(uncoveredCombination, getMatchedBruteForce(uncoveredCombination, bruteForce));
            covered++;
            System.out.println(covered + "/" + uncoveredCombinations.size());
        }

        return retValue;
    }
    
    
    private static List<Combination> getMatchedBruteForce(Combination uncoveredCombination, List<Combination> bruteForce) {
        List<Combination> retValue = new LinkedList<Combination>();

        for (Combination candidate : bruteForce) {
            if (CombinationUtils.matches(candidate, uncoveredCombination)) {
                retValue.add(candidate);
            }
        }

        return retValue;
    }
}
