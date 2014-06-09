/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Ray
 */
public class Jenny3 {

    private final ConstraintChecker checker;
    private final List<Domain> domains;

    public Jenny3(ConstraintChecker checker, List<Domain> domains) {
        this.checker = checker;
        this.domains = new LinkedList<Domain>(domains);
    }

    public List<Combination> solve(int n) {
        List<Combination> retValue = new LinkedList<Combination>();
        System.out.println("building N-Pairs");
        List<Combination> uncoveredCombinations = new NWiseCombinationBuilder2(domains, n).getCombinations();
        System.out.println("built " + uncoveredCombinations.size() + " N-Pairs");
        System.out.println("building brute force table");
        List<Combination> bruteForce = new NWiseCombinationBuilder2(domains, domains.size()).getCombinations();
        System.out.println("built brute force table");

        for (int i = 0; i < uncoveredCombinations.size() / 20; i++) {
            Combination nextBest = getRandom(bruteForce);
            retValue.add(nextBest);
            List<Combination> matchedCombinations = getMatchedCombinations(nextBest, uncoveredCombinations);
            if (matchedCombinations.size() > 0) {
                uncoveredCombinations.removeAll(matchedCombinations);
                System.out.println("next best matches " + matchedCombinations.size() + " combinations --> " + uncoveredCombinations.size() + " uncovered combinations remaining");
            }
        }

        System.out.println(uncoveredCombinations.size() + " combinations uncovered");
        while (!uncoveredCombinations.isEmpty() && !bruteForce.isEmpty()) {
            System.out.println("sorting ...");
            Collections.sort(bruteForce, sortByNumberOfMatches(uncoveredCombinations));
//            printNumberOfMatchedCombinations(bruteForce, uncoveredCombinations);

            for (int i = 0; i < 5; i++) {
                Combination nextBest = bruteForce.remove(0);

                List<Combination> matchedCombinations = getMatchedCombinations(nextBest, uncoveredCombinations);
                retValue.add(nextBest);
                if (matchedCombinations.size() > 0) {
                    uncoveredCombinations.removeAll(matchedCombinations);
                    System.out.println("next best matches " + matchedCombinations.size() + " combinations --> " + uncoveredCombinations.size() + " uncovered combinations remaining");
                }
            }
        }

        return retValue;
    }

    private Comparator<Combination> sortByNumberOfMatches(final List<Combination> uncoveredCombinations) {
        return new Comparator<Combination>() {
            Map<Combination, Integer> cache = new HashMap<Combination, Integer>();

            public int compare(Combination o1, Combination o2) {

                if (!cache.containsKey(o1)) {
                    cache.put(o1, getMatchCount(o1, uncoveredCombinations));
                }
                if (!cache.containsKey(o2)) {
                    cache.put(o2, getMatchCount(o2, uncoveredCombinations));
                }
                Integer m1 = cache.get(o1);
                Integer m2 = cache.get(o2);

                return m2.compareTo(m1);
            }

        };
    }

    private Integer getMatchCount(Combination o1, List<Combination> uncoveredCombinations) {
        return getMatchedCombinations(o1, uncoveredCombinations).size();
    }

    private List<Combination> getMatchedCombinations(Combination nextBest, List<Combination> uncoveredCombinations) {
        List<Combination> retValue = new LinkedList<Combination>();

        for (Combination candidate : uncoveredCombinations) {
            if (CombinationUtils.matches(nextBest, candidate)) {
                retValue.add(candidate);
            }
        }

        return retValue;
    }

    private void printNumberOfMatchedCombinations(List<Combination> bruteForce, List<Combination> uncoveredCombinations) {
        for (int i = 0; i < Math.min(6, bruteForce.size()); i++) {
            System.out.println("#" + i + " --> " + getMatchCount(bruteForce.get(i), uncoveredCombinations));
        }
    }

    private Combination getRandom(List<Combination> bruteForce) {
        Random random = new Random();
        return bruteForce.get(random.nextInt(bruteForce.size()));
    }

}
