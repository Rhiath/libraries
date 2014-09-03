/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ray
 */
public class SupportedProtocols {

    private final String protocol;
    private final String version;

    private final List<SupportedProtocols> subProtocols;

    public SupportedProtocols(String protocol, String version, SupportedProtocols... subProtocols) {
        this.protocol = protocol;
        this.version = version;
        this.subProtocols = new LinkedList<>();
        for (SupportedProtocols message : subProtocols) {
            this.subProtocols.add(message);
        }
    }

    public List<SupportedProtocols> getSubProtocols() {
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
