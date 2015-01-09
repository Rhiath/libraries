/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.storageeventtreetest;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author raymoon
 */
public class ServiceProvider implements ServiceInterface {

    private final List<String> knownIDs = new LinkedList<>();
    private final Map<String, List<String>> childMapping = new HashMap<>();
    private String root = null;
    private final ServiceObserver observer;

    public ServiceProvider(ServiceObserver observer) {
        this.observer = observer;
    }

    @Override
    public String getRootValue() {
        return root;
    }

    @Override
    public List<String> getNodeChildren(String value) {
        if (childMapping.containsKey(value)) {
            return new LinkedList<>(childMapping.get(value));
        } else {
            return new LinkedList<>();
        }

    }

    @Override
    public void put(String id) {
        knownIDs.add(id);

        rehash();
    }

    private void rehash() {
        childMapping.clear();


        List<String> lowerLayerIds = new LinkedList<>(knownIDs);
        List<String> nextLayerIds = new LinkedList<>();

        while (lowerLayerIds.size() > 1) {
            while (lowerLayerIds.size() > 1) {
                String id1 = lowerLayerIds.remove(0);
                String id2 = lowerLayerIds.remove(0);

                String parentHash = hash(id1, id2);

                List<String> children = new LinkedList<>();
                children.add(id1);
                children.add(id2);
                nextLayerIds.add(parentHash);

                childMapping.put(parentHash, children);
            }
            if (!lowerLayerIds.isEmpty()) {
                nextLayerIds.add(lowerLayerIds.get(0)); // if there are an odd number of IDs from the lower layer, we have to take over an ID
            }

            lowerLayerIds = nextLayerIds;
            nextLayerIds = new LinkedList<>();
        }
        if (lowerLayerIds.isEmpty()) {
            root = null;
        } else {
            root = lowerLayerIds.remove(0);
        }

        observer.newRootValue(); // inform service consumer about added value
    }

    private String hash(String id1, String id2) {
        return DigestUtils.md5Hex(id1 + "###" + id2);
    }

    @Override
    public void remove(String id) {
        knownIDs.remove(id);

        rehash();
    }
}
