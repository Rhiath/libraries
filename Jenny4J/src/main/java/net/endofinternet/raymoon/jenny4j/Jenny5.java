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
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Ray
 */
public class Jenny5 {

    private final ConstraintChecker checker;
    private final List<Domain> domains;

    public Jenny5(ConstraintChecker checker, List<Domain> domains) {
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
        Map<Combination, Set<Combination>> bruteForceByNPair;// = buildBruteForceLookup(bruteForce, uncoveredCombinations);

        System.out.println(uncoveredCombinations.size() + " combinations need to be covered in test cases");

        System.out.println("building brute force lookup");
        bruteForceByNPair = convert(BruteForceLookupBuilder3.buildBruteForceLookup(bruteForce, uncoveredCombinations, n));
        System.out.println("built brute force lookup");
        while (!uncoveredCombinations.isEmpty() && !bruteForce.isEmpty()) {

            System.out.println("sorting uncovered combinations by number of combinations they match");
            Collections.sort(uncoveredCombinations, sortByNumberOfMatches(bruteForceByNPair));
            for (int i = 0; i < 1 && !uncoveredCombinations.isEmpty(); i++) {

                Combination nextBestNPair = uncoveredCombinations.get(0);
                List<Combination> bruteForceSublist = new LinkedList<Combination>(bruteForceByNPair.get(nextBestNPair));
//                System.out.println("there are " + bruteForceSublist.size() + " combinations for the NPair that matches the most combinations");

                Collections.sort(bruteForceSublist, sortByNumberOfMatches(uncoveredCombinations));

                Combination nextBest;

                nextBest = bruteForceSublist.get(0);

                List<Combination> matchedCombinations = getMatchedCombinations(nextBest, uncoveredCombinations);
                retValue.add(nextBest);
                if (matchedCombinations.size() > 0) {
                    uncoveredCombinations.removeAll(matchedCombinations);
                    for (Combination matchesNPair : matchedCombinations) {
                        Set<Combination> bruteForceCasesToUpdate = bruteForceByNPair.remove(matchesNPair);
//                        for (Combination bruteForceCombination : bruteForceCasesToUpdate) {
//                            for (Combination matchedNPair : BruteForceLookupBuilder3.buildNPairCandidates(bruteForceCombination, n)) {
//                                if (bruteForceByNPair.containsKey(matchedNPair)) {
//                                    bruteForceByNPair.get(matchedNPair).remove(bruteForceCombination);
//                                }
//                            }
//                        }
                    }
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

//    private List<Combination> getMatchedBruteForce(Combination uncoveredCombination, List<Combination> bruteForce) {
//        List<Combination> retValue = new LinkedList<Combination>();
//
//        for (Combination candidate : bruteForce) {
//            if (CombinationUtils.matches(candidate, uncoveredCombination)) {
//                retValue.add(candidate);
//            }
//        }
//
//        return retValue;
//    }
    private Comparator<Combination> sortByNumberOfMatches(final Map<Combination, Set<Combination>> bruteForceByNPair) {
        return new Comparator<Combination>() {
            public int compare(Combination o1, Combination o2) {

                Integer m1 = bruteForceByNPair.get(o2).size();
                Integer m2 = bruteForceByNPair.get(o1).size();

                return m2.compareTo(m1);
            }

        };
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

//    private Map<Combination, List<Combination>> buildBruteForceLookup(List<Combination> bruteForce, List<Combination> uncoveredCombinations) {
//        Map<Combination, List<Combination>> retValue = new HashMap<Combination, List<Combination>>();
//
//        int covered = 0;
//        for (Combination uncoveredCombination : uncoveredCombinations) {
//            retValue.put(uncoveredCombination, getMatchedBruteForce(uncoveredCombination, bruteForce));
//            covered++;
//            System.out.println(covered+"/"+uncoveredCombinations.size());
//        }
//
//        return retValue;
//    }
    private Map<Combination, Set<Combination>> convert(Map<Combination, List<Combination>> buildBruteForceLookup) {
        Map<Combination, Set<Combination>> retValue = new HashMap<Combination, Set<Combination>>();

        int processedBruteForceCount = 0;
        int lastPercent = 0;
        for (Entry<Combination, List<Combination>> entry : buildBruteForceLookup.entrySet()) {
            retValue.put(entry.getKey(), new HashSet<Combination>(entry.getValue()));
            processedBruteForceCount++;

            int percent = (processedBruteForceCount * 100) / (buildBruteForceLookup.size());
            if (percent - lastPercent >= 5) {
                System.out.println(percent + " % of brute force conversion is done");
                lastPercent = percent;
            }
        }

        return retValue;
    }
}
