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

/**
 *
 * @author Ray
 */
public class Jenny {
    
    private final ConstraintChecker checker;
    private final List<Domain> domains;
    
    public Jenny(ConstraintChecker checker, List<Domain> domains) {
        this.checker = checker;
        this.domains = new LinkedList<Domain>(domains);
    }
    
    public List<Combination> solve() {
        List<Combination> retValue = new LinkedList<Combination>();
        List<Combination> uncoveredCombinations = new NWiseCombinationBuilder(domains, 2).getCombinations();
        
        Map<Integer, Set<Combination>> partialSolutions = new HashMap<Integer, Set<Combination>>();
        for (int i = 0; i <= domains.size(); i++) {
            partialSolutions.put(i, new HashSet<Combination>());
        }
        partialSolutions.put(1, new HashSet<Combination>(new NWiseCombinationBuilder(domains, 1).getCombinations()));
        
        for (int i = 2; i <= domains.size(); i++) { // for all missing sizes
            // take any two sizes that for the next required size
            for (int sizeA = 0; sizeA < i; sizeA++) {
                for (int sizeB = sizeA; sizeB < i; sizeB++) {
                    if (sizeA + sizeB == i) {
                        
                        for (Combination combA : partialSolutions.get(sizeA)) {
                            for (Combination combB : partialSolutions.get(sizeB)) {
                                
                                if (CombinationUtils.isDistinct(combA, combB)) {
                                    final Combination combination = CombinationUtils.combine(combA, combB);
                                    if (checker.isValid(combination)) {
                                        partialSolutions.get(i).add(combination);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return new LinkedList<Combination>(partialSolutions.get(domains.size()));
    }
    
}
