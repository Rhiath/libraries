/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.messages;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ray
 */
public class SupportedProtocolMessage implements Serializable {

    private final String protocol;
    private final String version;

    private final List<SupportedProtocolMessage> subProtocols;

    public SupportedProtocolMessage(String protocol, String version, SupportedProtocolMessage... subProtocols) {
        this.protocol = protocol;
        this.version = version;
        this.subProtocols = new LinkedList<>();
        for (SupportedProtocolMessage message : subProtocols) {
            this.subProtocols.add(message);
        }
    }

    public List<SupportedProtocolMessage> getSubProtocols() {
        return subProtocols;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ProtocolMessage: " + protocol + "@" + version + " [...]";
    }

}
