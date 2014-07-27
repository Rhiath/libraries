/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode.messages;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ray
 */
public class ProtocolDenominator {

    public static SupportedProtocolMessage getCommonDenominator(SupportedProtocolMessage supportedLocally, SupportedProtocolMessage supportedRemotely) throws NoCommonProtocolStackException {
        if (equalWithoutRecursion(supportedLocally, supportedRemotely)) {
            return new SupportedProtocolMessage(supportedLocally.getProtocol(), supportedLocally.getVersion(), buildCommonDenominator(supportedLocally.getSubProtocols(), supportedRemotely.getSubProtocols()));
        } else {
            throw new NoCommonProtocolStackException("local protocol is " + supportedLocally.getProtocol() + "@" + supportedLocally.getVersion() + ", remote protocol is " + supportedRemotely.getProtocol() + "@" + supportedRemotely.getVersion());
        }
    }

    private static boolean equalWithoutRecursion(SupportedProtocolMessage a, SupportedProtocolMessage b) {
        return a.getProtocol().equals(b.getProtocol()) && a.getVersion().equals(b.getVersion());
    }

    private static SupportedProtocolMessage[] buildCommonDenominator(List<SupportedProtocolMessage> localSubs, List<SupportedProtocolMessage> remoteSubs) {
        List<SupportedProtocolMessage> common = new LinkedList<>();
        for (SupportedProtocolMessage localSub : localSubs) {
            for (SupportedProtocolMessage remoteSub : remoteSubs) {
                if (equalWithoutRecursion(localSub, remoteSub)) {
                    common.add(new SupportedProtocolMessage(localSub.getProtocol(), localSub.getVersion(), buildCommonDenominator(localSub.getSubProtocols(), remoteSub.getSubProtocols())));
                }
            }
        }

        SupportedProtocolMessage[] retrValue = new SupportedProtocolMessage[common.size()];
        for (int i = 0; i < retrValue.length; i++) {
            retrValue[i] = common.get(i);
        }
        return retrValue;
    }

}
