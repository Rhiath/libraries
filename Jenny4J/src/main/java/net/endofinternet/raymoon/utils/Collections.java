/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Ray
 */
public class Collections {

    public static <T> List<T> asList(T... values) {
        return Arrays.asList(values);
    }

    public static <T> Set<T> asSet(T... values) {
        return new HashSet<T>(asList(values));
    }

    public static <T> List<T> union(List<T>... lists) {
        Set<T> retValue = new HashSet<T>(lists[0]);
        for (int i = 1; i < lists.length; i++) {
            retValue.retainAll(lists[i]);
        }

        return new LinkedList<T>(retValue);
    }

    public static <T> List<T> unionLists(List<List<T>> lists) {
        List<List<T>> listsCopy = new LinkedList<List<T>>(lists);

        java.util.Collections.sort(listsCopy, new Comparator<List<T>>() {

            public int compare(List<T> o1, List<T> o2) {
                Integer l1 = o1.size();
                Integer l2 = o2.size();

                return l1.compareTo(l2);
            }
        });

        return unionListsByLength(listsCopy);
    }

    private static <T> List<T> unionListsByLength(List<List<T>> lists) {
        List<Set<T>> sets = new LinkedList<Set<T>>();
        for ( List<T> list : lists){
            sets.add(new HashSet<T>(list));
        }
        
        Set<T> first = sets.get(0);
        for (int i = 0; i < sets.size(); i++) {
            first = unionSets(first, sets.get(i));
        }

        return new LinkedList<T>(first);
    }

    private static <T> Set<T> unionSets(Set<T> listsA, Set<T> listsB) {
        Set<T> retValue = new HashSet<T>(listsB);
        for (T t : listsA) {
            if (!retValue.contains(t)) {
                retValue.remove(t);
            }
        }

        return retValue;
    }
}
