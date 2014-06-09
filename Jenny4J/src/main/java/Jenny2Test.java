/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import net.endofinternet.raymoon.jenny4j.Combination;
import net.endofinternet.raymoon.jenny4j.CombinationUtils;
import net.endofinternet.raymoon.jenny4j.ConstraintChecker;
import net.endofinternet.raymoon.jenny4j.Domain;
import net.endofinternet.raymoon.jenny4j.Jenny;
import net.endofinternet.raymoon.jenny4j.NWiseCombinationBuilder;
import net.endofinternet.raymoon.jenny4j.ValueForPosition;

/**
 *
 * @author Ray
 */
public class Jenny2Test {

    public static void main(String[] args) {
//        Domain domain1 = new Domain(1, 10);
//        Domain domain2 = new Domain(2, 13);
//        Domain domain3 = new Domain(3, 5);
//        Domain domain4 = new Domain(4, 20);
//        Domain domain5 = new Domain(5, 10);
//        Domain domain6 = new Domain(6, 10);
        final List<Domain> domainList = new LinkedList<Domain>();
        for (int i = 0; i < 6; i++) {
           domainList.add(new Domain(i, 6));
            
        }

        int n = 2;
        
        Jenny jenny = new Jenny(new ConstraintChecker() {

            public boolean isValid(Combination combination) {
                return true;
            }
        }, domainList);

        List<Combination> combinations = jenny.solve(n);
        System.out.println("solved with " + combinations.size() + " combinations");
//        printSolution(combinations);

        List<Combination> npairsToMatch = new NWiseCombinationBuilder(domainList, n).getCombinations();
        validate(combinations, npairsToMatch);

    }

    private static void printSolution(List<Combination> combinations) {
        for (Combination combination : combinations) {
            List<ValueForPosition> values = new LinkedList<ValueForPosition>(combination.getValues());
            java.util.Collections.sort(values, new Comparator<ValueForPosition>() {

                public int compare(ValueForPosition o1, ValueForPosition o2) {
                    Integer v1 = o1.getPosition();
                    Integer v2 = o2.getPosition();

                    return v1.compareTo(v2);
                }
            });
            for (ValueForPosition vfp : values) {
//                System.out.print("v["+vfp.getPosition()+"] ");
                System.out.print(vfp.getValue() + "  ");
            }
            System.out.println("");
        }
    }

    private static void validate(List<Combination> combinations, List<Combination> npairsToMatch) {
        boolean allAreMatched = true;
        for (Combination npair : npairsToMatch) {
            boolean isMatched = false;

            for (Combination combination : combinations) {
                if (CombinationUtils.matches(combination, npair)) {
                    isMatched = true;
//                    System.out.println("npair " + getText(npair) + " is matched by combination " + getText(combination));
                }
            }

            allAreMatched &= isMatched;

            if (!isMatched) {
                System.out.println("[ERROR] an npair is not met by any of the combinations!");
            }
        }
        if (!allAreMatched) {
            System.out.println("[ERROR] at least one npair was not matched by a combination");
        } else {
            System.out.println("all npairs were matched by a test case");
        }
    }

    private static String getText(Combination npair) {
        StringBuffer buffer = new StringBuffer();

        List<ValueForPosition> values = new LinkedList<ValueForPosition>(npair.getValues());
        java.util.Collections.sort(values, new Comparator<ValueForPosition>() {

            public int compare(ValueForPosition o1, ValueForPosition o2) {
                Integer v1 = o1.getPosition();
                Integer v2 = o2.getPosition();

                return v1.compareTo(v2);
            }
        });
        for (ValueForPosition vfp : values) {
            buffer.append("v[" + vfp.getPosition() + "] ");
            buffer.append(vfp.getValue() + "  ");
        }

        return buffer.toString();
    }
}
