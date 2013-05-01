/**
 * Copyright 2011 ABNF Parser Generator Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package apg.automata;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import util.Matrix;

public class DFA {

    private Matrix<Integer> transition;
    private int state;
    private Set<Integer> acceptStates;

    public DFA(Matrix<Integer> transition, Set<Integer> acceptStates) {
        this.transition = transition;
        this.state = 0;
        this.acceptStates = acceptStates;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Matrix<Integer> getTransitionTable() {
        return this.transition;
    }

    public Set<Integer> getAcceptStates() {
        return this.acceptStates;
    }

    public boolean isMatch() {
        return state != -1 && this.acceptStates.contains(this.state);
    }

    public void step(int character) {
        if (state != -1) {
            Integer nextState = this.transition.get(this.state, character);
            if (nextState == null) {
                nextState = -1;
            }

            this.state = nextState;
        }
    }

    public List<Integer> out() {
        List<Integer> values = new ArrayList<Integer>();

        for (int i = 0; i < this.transition.colSize(); i++) {
            Integer nextState = this.transition.get(this.state, i);
            if (nextState != null) {
                values.add(i);
            }
        }

        return values;
    }
}