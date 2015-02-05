/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.gridtools;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author raymoon
 */
public class DatagramPool {

    private final List<DatagramPacket> resources;
    private final Semaphore limiter;

    public DatagramPool(int packetCount, int packetSize) {
        resources = new LinkedList<DatagramPacket>();
        limiter = new Semaphore(packetCount);

        for (int i = 0; i < packetCount; i++) {
            resources.add(new DatagramPacket(new byte[packetSize], packetSize));
        }
    }

    public DatagramPacket getNextPacket() {
        limiter.acquireUninterruptibly();
        synchronized (resources) {
            return resources.remove(0);
        }
    }

    public void freePacket(DatagramPacket packet) {
        synchronized (resources) {
            this.resources.add(packet);
        }
        limiter.release();
    }
}
