/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools;

import java.net.DatagramPacket;

/**
 *
 * @author raymoon
 */
public class Packet {
    private final DatagramPool pool;
    private final DatagramPacket packet;

    public Packet(DatagramPool pool, DatagramPacket packet) {
        this.pool = pool;
        this.packet = packet;
    }
    
    public void dispose(){
        pool.freePacket(packet);
    }
    
    public byte[] getDate(){
        return packet.getData();
    }
    
    public int getLength(){
        return packet.getLength();
    }
    
    public int getOffset(){
        return packet.getOffset();
    }
    
    public int getPort(){
        return packet.getPort();
    }
    
    public byte[] getSenderAddress(){
        return packet.getAddress().getAddress();
    }
}
