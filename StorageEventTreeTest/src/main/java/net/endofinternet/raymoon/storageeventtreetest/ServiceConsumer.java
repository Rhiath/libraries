/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.storageeventtreetest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author raymoon
 */
public class ServiceConsumer implements ServiceObserver {

    private ServiceInterface provider;
    private String knownRoot = null;
    private final List<String> knownIDs = new LinkedList<>();
    private final Map<String, List<String>> childMapping = new HashMap<>();

    public void setProvider(ServiceInterface provider) {
        this.provider = provider;
    }

    @Override
    public void newRootValue() {
        String providerRoot = provider.getRootValue();


        if (!same(providerRoot, knownRoot)) {
            long t0 = System.currentTimeMillis();
            getUpToDate(providerRoot);
            long t1 = System.currentTimeMillis();

            System.out.println("rebuilding took " + (t1 - t0) + " msec");
        }
    }

    private void getUpToDate(String rootValue) {
        knownRoot = rootValue;

        Map<String, List<String>> oldChildMapping = new HashMap<>(childMapping);
        childMapping.clear();


        List<String> idsToProcess = new LinkedList<>();
        idsToProcess.add(rootValue);

        while (!idsToProcess.isEmpty()) {
            String nextToProcess = idsToProcess.remove(0);
            List<String> children = provider.getNodeChildren(nextToProcess);
            if (nextToProcess != null) {
                childMapping.put(nextToProcess, children);

                for (String child : children) {
                    if (oldChildMapping.containsKey(child)) {
//                    System.out.println("id '"+child+"' is known, reusing local knowledge");
                        takeOver(child, oldChildMapping);
                    } else {
                        idsToProcess.add(child);
                    }
                }
            }
        }

        System.out.println("provider has new root value ('" + knownRoot + "'), brought self up to date");
        knownIDs.clear();
        for (String key : childMapping.keySet()) {
            if (childMapping.get(key).isEmpty()) {
                knownIDs.add(key);
//                System.out.println(key);
            }
        }
        System.out.println("now knowing about " + knownIDs.size() + " id(s)");
        System.out.println("#############");
    }

    private void takeOver(String child, Map<String, List<String>> oldChildMapping) {
        childMapping.put(child, oldChildMapping.get(child));

        for (String childChild : oldChildMapping.get(child)) {
            takeOver(childChild, oldChildMapping);
        }
    }

    private boolean same(String providerRoot, String knownRoot) {
        if (providerRoot == null) {
            if (knownRoot == null) {
                return true;
            } else {
                return false;
            }
        } else {
            return providerRoot.equals(knownRoot);
        }
    }
}
