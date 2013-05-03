/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raytec.gridtools.messaging;

import de.raytec.java.lib.exceptions.InvalidContentException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 *
 * @author raymoon
 */
public class ReceiverEndpointAnnouncement implements Message {

    private final Inet4Address address;
    private final short port;
    
    public static MessageDecoder<ReceiverEndpointAnnouncement> getDecoder(){
        return new MessageDecoder<ReceiverEndpointAnnouncement>() {

            public int getMessageSize() {
                return 4 + 2;
            }

            public byte getOpCode() {
                return 1;
            }

            public byte getVersion() {
                return 1;
            }

            public ReceiverEndpointAnnouncement decode(ByteBuffer buffer) throws InvalidContentException {
                byte[] address = new byte[4];
                buffer.get(address);
                
                short port = buffer.getShort();
                try {
                    return new ReceiverEndpointAnnouncement((Inet4Address) Inet4Address.getByAddress(address), port);
                } catch (UnknownHostException ex) {
                    throw new InvalidContentException("invalid address content", ex);
                }
            }
        };
    }
    
    public ReceiverEndpointAnnouncement(Inet4Address address, short port) {
        super();

        this.address = address;
        this.port = port;
    }

    public Inet4Address getAddress() {
        return address;
    }

    public short getPort() {
        return port;
    }
}
