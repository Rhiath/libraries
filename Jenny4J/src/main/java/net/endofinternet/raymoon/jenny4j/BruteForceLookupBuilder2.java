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
import net.endofinternet.raymoon.utils.Collections;

/**
 *
 * @author Ray
 */
public class BruteForceLookupBuilder2 {

    public static Map<Combination, List<Combination>> buildBruteForceLookup(List<Combination> bruteForce, List<Combination> uncoveredCombinations) {
        Map<Combination, List<Combination>> retValue = new HashMap<Combination, List<Combination>>();

        Map<ValueForPosition, List<Combination>> bruteForceWithValue = new HashMap<ValueForPosition, List<Combination>>();
        for (Combination bruteForceInstance : bruteForce) {
            for (ValueForPosition vfp : bruteForceInstance.getValues()) {

                if (!bruteForceWithValue.containsKey(vfp)) {
                    bruteForceWithValue.put(vfp, new LinkedList<Combination>());
                }
                bruteForceWithValue.get(vfp).add(bruteForceInstance);
            }
        }
        System.out.println("built brute force lookup per single value");
        
        int coveredCount = 0;
        for (Combination uncovered : uncoveredCombinations) {
            List<List<Combination>> combinations = new LinkedList<List<Combination>>();
            for (ValueForPosition vfp : uncovered.getValues()) {
                combinations.add(bruteForceWithValue.get(vfp));
            }
            System.out.print("gathered brute force items that match per list:");
            for ( List<Combination> combination : combinations ){
                System.out.print("  "+combination.size());
            }
            System.out.println("");
            
            List<Combination> bruteForceInstamces = Collections.unionLists(combinations);
            retValue.put(uncovered, bruteForceInstamces);
            
            coveredCount++;
            System.out.println(coveredCount + "/" + uncoveredCombinations.size());
        }

        return retValue;
    }
}
