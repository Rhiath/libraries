/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools.remoteOperations;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author raymoon
 */
public class MemoryStorage {
    private final int id;
    private final Map<Long, byte[]> storedData = new HashMap<Long, byte[]>();

    public MemoryStorage(int id) {
        this.id = id;
    }
    
    public byte[] getData(long block) {
        return storedData.get(Long.valueOf(block));
    }
    public void putData(long block, byte[] data) {
         storedData.put(Long.valueOf(block), data);
    }
}
