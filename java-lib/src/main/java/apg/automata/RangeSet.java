/**
 * Copyright 2011 ABNF Parser Generator Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package apg.automata;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Set of character ranges.
 */
public class RangeSet implements Iterable<Range> {
    private List<Range> ranges;

    public RangeSet() {
        this.ranges = new LinkedList<Range>();
    }

    public void add(Range e) {
        this.ranges.add(e);
    }

    public boolean contains(int character) {
        for ( Range r  :ranges ){
            if ( r.min() <= character && r.max() >= character){
                return true;
            }
        }
        
        return false;
    }

    public Iterator<Range> iterator() {
       return ranges.iterator();
    }
}