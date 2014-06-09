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
import java.util.Set;
import net.endofinternet.raymoon.utils.Collections;

/**
 *
 * @author Ray
 */
public class Jenny2 {
    
    private final ConstraintChecker checker;
    private final List<Domain> domains;
    
    public Jenny2(ConstraintChecker checker, List<Domain> domains) {
        this.checker = checker;
        this.domains = new LinkedList<Domain>(domains);
    }
    
    public List<Combination> solve(int n) {
        List<Combination> uncoveredCombinations = new NWiseCombinationBuilder(domains, n).getCombinations();
        List<Combination> retValue = new LinkedList<Combination>();
        
        Map<Integer, List<Combination>> combinationsOfSizeN = new HashMap<Integer, List<Combination>>();
        Map<Combination, List<Combination>> missingNWiseCoveredByCombination = new HashMap<Combination, List<Combination>>();
        for (int i = 1; i <= domains.size(); i++) {
            combinationsOfSizeN.put(i, new LinkedList<Combination>());
        }
        for (Combination nwise : uncoveredCombinations) {
            missingNWiseCoveredByCombination.put(nwise, Collections.asList(nwise));
            combinationsOfSizeN.get(n).add(nwise);
        }
        
        while (!uncoveredCombinations.isEmpty()) {
            Combination best = getBest(domains.size(), missingNWiseCoveredByCombination, combinationsOfSizeN);
            
            if (best == null) {
                System.out.println("no more solutions?");
            } else {
                retValue.add(best);
                
                uncoveredCombinations.removeAll(missingNWiseCoveredByCombination.get(best));
                regrade(best, missingNWiseCoveredByCombination);
            } 
        }
        
        return combinationsOfSizeN.get(domains.size());
    }
    
    private Combination getBest(int size, Map<Combination, List<Combination>> missingNWiseCoveredByCombination, Map<Integer, List<Combination>> combinationsOfSizeN, Combination... blacklist) {
        for (int a = 1; a < size; a++) {
            for (int b = 1; b < size; b++) {
                Combination combA = getBest(a, missingNWiseCoveredByCombination, combinationsOfSizeN);
                Combination combB = getBest(b, missingNWiseCoveredByCombination, combinationsOfSizeN, combA);
                
                if (combA != null && combB != null) {
                    if (CombinationUtils.isMergeable(combA, combB)) {
                        Combination combination = CombinationUtils.combine(combA, combB);
                        if (!missingNWiseCoveredByCombination.containsKey(combination)) {
                            Set<Combination> matchedCombinations = new HashSet<Combination>();
                            matchedCombinations.addAll(missingNWiseCoveredByCombination.get(combA));
                            matchedCombinations.addAll(missingNWiseCoveredByCombination.get(combB));
                            missingNWiseCoveredByCombination.put(combination, new LinkedList<Combination>(matchedCombinations));
                        }
                    }
                }
            }
        }
        
        List<Combination> blacklistList = Collections.asList(blacklist);
        Combination best = null;
        int covered = -1;
        for (Combination candidate : combinationsOfSizeN.get(size)) {
            if (!blacklistList.contains(candidate)) {
                if (missingNWiseCoveredByCombination.get(candidate).size() > covered) {
                    best = candidate;
                    covered = missingNWiseCoveredByCombination.get(candidate).size();
                }
            }
        }
        
        return best;
    }
    
    private void regrade(Combination best, Map<Combination, List<Combination>> missingNWiseCoveredByCombination) {
        List<Combination> nwiseCovered = missingNWiseCoveredByCombination.get(best);
        final Set<Combination> keys = missingNWiseCoveredByCombination.keySet();
        
        for (Combination other : keys) {
            List<Combination> didCover = missingNWiseCoveredByCombination.get(best);
            List<Combination> doesCover = new LinkedList<Combination>(didCover);
            doesCover.removeAll(missingNWiseCoveredByCombination.get(best));
            missingNWiseCoveredByCombination.put(other, doesCover);
        }
    }
}
