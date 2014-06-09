/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.jenny4j;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.endofinternet.raymoon.utils.Collections;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Ray
 */
public class NWiseCombinationBuilderTest {

    @Test
    public void testOneValueDomain() {
        Domain domain = new Domain(1, 4);
        List<Combination> combinations = new NWiseCombinationBuilder2(Collections.asList(domain), 1).getCombinations();

        Set<Integer> missingValues = Collections.asSet(0, 1, 2, 3);

        for (Combination combination : combinations) {
            Assert.assertTrue(combination.getValues().size() == 1);
            
            ValueForPosition vfp = combination.getValues().iterator().next();
            
            Assert.assertTrue(vfp.getPosition() == domain.getPosition());
            Assert.assertTrue(vfp.getValue() >= 0);
            Assert.assertTrue(vfp.getValue() < domain.getNumberOfPossibleValues());

            Assert.assertTrue(missingValues.contains(vfp.getValue()));
            missingValues.remove(vfp.getValue());
        }
        assertThat(missingValues.size(), is(0));
    }

    @Test
    public void tesTwoDomains() {
        Domain domain1 = new Domain(1, 2);
        Domain domain2 = new Domain(2, 2);
        List<Combination> combinations = new NWiseCombinationBuilder2(Collections.asList(domain1, domain2), 1).getCombinations();

        Set<Combination> missingCombinations = Collections.asSet(
                new Combination(Collections.asSet(new ValueForPosition(1, 0))),
                new Combination(Collections.asSet(new ValueForPosition(1, 1))),
                new Combination(Collections.asSet(new ValueForPosition(2, 0))),
                new Combination(Collections.asSet(new ValueForPosition(2, 1)))
        );

        for (Combination combination : combinations) {
            Assert.assertTrue(combination.getValues().size() == 1);

            final ValueForPosition vfp = combination.getValues().iterator().next();

            Assert.assertTrue(vfp.getPosition() == 1 || vfp.getPosition() == 2);

            Assert.assertTrue(vfp.getValue() >= 0);
            Assert.assertTrue(vfp.getValue() < 2);

            Assert.assertTrue(missingCombinations.contains(combination));
            missingCombinations.remove(combination);
        }
        assertThat(missingCombinations.size(), is(0));
    }

    @Test
    public void testThreeDomainsPairwise() {
        Domain domain1 = new Domain(1, 2);
        Domain domain2 = new Domain(2, 2);
        Domain domain3 = new Domain(3, 2);
        List<Combination> combinations = new NWiseCombinationBuilder2(Collections.asList(domain1, domain2, domain3), 2).getCombinations();

        List<ValueForPosition> possibility = new LinkedList<ValueForPosition>();
        possibility.add(new ValueForPosition(1, 0));
        possibility.add(new ValueForPosition(1, 1));
        possibility.add(new ValueForPosition(2, 0));
        possibility.add(new ValueForPosition(2, 1));
        possibility.add(new ValueForPosition(3, 0));
        possibility.add(new ValueForPosition(3, 1));

        Set<Combination> missingCombinations = new HashSet<Combination>();
        for (int i = 0; i < possibility.size(); i++) {
            for (int j = i + 1; j < possibility.size(); j++) {
                if (distinct(possibility.get(i).getPosition(), possibility.get(j).getPosition())) {
                    missingCombinations.add(new Combination(Collections.asSet(possibility.get(i), possibility.get(j))));
                }
            }
        }

        for (Combination combination : combinations) {
            Assert.assertTrue(combination.getValues().size() == 2);
            Assert.assertTrue(missingCombinations.contains(combination));
            missingCombinations.remove(combination);
        }
        assertThat(missingCombinations.size(), is(0));
    }

    @Test
    public void tesThreeDomainsTriwise() {
        Domain domain1 = new Domain(1, 2);
        Domain domain2 = new Domain(2, 2);
        Domain domain3 = new Domain(3, 2);
        List<Combination> combinations = new NWiseCombinationBuilder2(Collections.asList(domain1, domain2, domain3), 3).getCombinations();

        List<ValueForPosition> possibility = new LinkedList<ValueForPosition>();
        possibility.add(new ValueForPosition(1, 0));
        possibility.add(new ValueForPosition(1, 1));
        possibility.add(new ValueForPosition(2, 0));
        possibility.add(new ValueForPosition(2, 1));
        possibility.add(new ValueForPosition(3, 0));
        possibility.add(new ValueForPosition(3, 1));

        Set<Combination> missingCombinations = new HashSet<Combination>();
        for (int i = 0; i < possibility.size(); i++) {
            for (int j = i + 1; j < possibility.size(); j++) {
                for (int k = j + 1; k < possibility.size(); k++) {
                    if (distinct(possibility.get(i).getPosition(), possibility.get(j).getPosition(), possibility.get(k).getPosition())) {
                        missingCombinations.add(new Combination(Collections.asSet(possibility.get(i), possibility.get(j), possibility.get(k))));
                    }
                }
            }
        }

        for (Combination combination : combinations) {
            Assert.assertTrue(combination.getValues().size() == 3);
            Assert.assertTrue(missingCombinations.contains(combination));
            missingCombinations.remove(combination);
        }
        assertThat(missingCombinations.size(), is(0));
    }

    private boolean distinct(int... values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i] == values[j]) {
                    return false;
                }
            }
        }

        return true;
    }
}
