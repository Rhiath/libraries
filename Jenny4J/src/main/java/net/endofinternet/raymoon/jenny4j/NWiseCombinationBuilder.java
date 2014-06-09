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
        List<Combination> retValue = new LinkedList<Combination>();

        for (List<Domain> domainCombination : buildDomainCombinations()) {
//            printDomainList(domainCombination);
            buildCombinations(retValue, domainCombination);
        }

        return retValue;
    }

    private List<List<Domain>> buildDomainCombinations() {
        List<List<Domain>> retValue = new LinkedList<List<Domain>>();
        List<Domain> remainingDomains = new LinkedList<Domain>(domains);

        recurse(retValue, new LinkedList<Domain>(), remainingDomains, combinationSize);

        System.out.println("found " + retValue.size() + " combinations of any " + combinationSize + " domains");

        return retValue;
    }

    private void recurse(List<List<Domain>> retValue, List<Domain> chosenDomains, List<Domain> remainingDomains, int recursionDepth) {
        if (recursionDepth == 0) {
            retValue.add(new LinkedList<Domain>(chosenDomains));
        } else {
            for (int i = 0; i < remainingDomains.size(); i++) {
                List<Domain> remainingDomainsSublist = new LinkedList<Domain>();
                for (int j = i + 1; j < remainingDomains.size(); j++) {
                    remainingDomainsSublist.add(remainingDomains.get(j));
                }
                List<Domain> chosenDomainsSubList = new LinkedList<Domain>(chosenDomains);
                chosenDomainsSubList.add(remainingDomains.get(i));
                recurse(retValue, chosenDomainsSubList, remainingDomainsSublist, recursionDepth - 1);
            }
//            for (Domain candidate : remainingDomains) {
//                List<Domain> remainingDomainsSublist = new LinkedList<Domain>(remainingDomains);
//                remainingDomainsSublist.remove(candidate);
//                recurse(retValue, remainingDomainsSublist, recursionDepth - 1);
//            }

        }
    }

    private void buildCombinations(List<Combination> retValue, List<Domain> domainCombination) {
        recurse(retValue, domainCombination, new HashSet<ValueForPosition>());
        System.out.println("built " + retValue.size() + " combinations");
    }

    private void recurse(List<Combination> retValue, List<Domain> remainingDomains, HashSet<ValueForPosition> hashSet) {
        if (remainingDomains.isEmpty()) {
            retValue.add(new Combination(hashSet));

            if (retValue.size() % 100000 == 0) {
                System.out.println("built " + retValue.size() + " combinations so far");
            }
        } else {
            Domain currentDomain = remainingDomains.get(0);
            List<Domain> remainingDomainsSublist = new LinkedList<Domain>(remainingDomains);
            remainingDomainsSublist.remove(currentDomain);

            for (int i = 0; i < currentDomain.getNumberOfPossibleValues(); i++) {
                HashSet<ValueForPosition> hashSetCopy = new HashSet<ValueForPosition>(hashSet);
                hashSetCopy.add(new ValueForPosition(currentDomain.getPosition(), i));

                recurse(retValue, remainingDomainsSublist, hashSetCopy);
            }
        }
    }

    private void printDomainList(List<Domain> domainCombination) {
        System.out.println("domain list: ");
        for (Domain domain : domainCombination) {
            System.out.print(domain.getPosition() + " ");
        }
        System.out.println("");
    }

}
