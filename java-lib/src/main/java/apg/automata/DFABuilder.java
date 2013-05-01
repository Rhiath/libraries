/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apg.automata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import util.MappedMatrix;
import util.Matrix;

/**
 *
 * @author raymoon
 */
public class DFABuilder {

    public static DFA buildDFA(Program prog, Range alphabet) {

        Matrix<Integer> transition = new MappedMatrix<Integer>();
        int state = 0;
        Set<Integer> acceptStates =  new TreeSet<Integer>();

        List<Set<Integer>> states = new ArrayList<Set<Integer>>();

        states.add(
                new NFA(prog).state());
        if (states.get(
                0).contains(-1)) {
            acceptStates.add(0);
        }
        int unmarkedState = 0;

        while (unmarkedState < states.size()) {
            for (int c = alphabet.min(); c <= alphabet.max(); c++) {
                NFA fa = new NFA(prog, states.get(unmarkedState));
                fa.step(c);

                Set<Integer> to = fa.state();
                if (to.size() != 0) {
                    int nextState = states.indexOf(to);
                    if (nextState == -1) {
                        states.add(to);
                        nextState = states.size() - 1;

                        if (to.contains(-1)) {
                            acceptStates.add(nextState);
                        }
                    }

                    transition.put(unmarkedState, c, nextState);
//                    this.reverseTransition.put(nextState, c, unmarkedState);
                }
            }

            unmarkedState++;
        }
        
        return new DFA(transition, acceptStates);
    }
}
