/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.netcoms;

import net.endofinternet.raymoon.netcoms.messages.exceptions.NoCommonProtocolStackException;
import net.endofinternet.raymoon.netcoms.SupportedProtocols;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ray
 */
public class ProtocolDenominator {

    public static SupportedProtocols getCommonDenominator(SupportedProtocols supportedLocally, SupportedProtocols supportedRemotely) throws NoCommonProtocolStackException {
        if (equalWithoutRecursion(supportedLocally, supportedRemotely)) {
            return new SupportedProtocols(supportedLocally.getProtocol(), supportedLocally.getVersion(), buildCommonDenominator(supportedLocally.getSubProtocols(), supportedRemotely.getSubProtocols()));
        } else {
            throw new NoCommonProtocolStackException("local protocol is " + supportedLocally.getProtocol() + "@" + supportedLocally.getVersion() + ", remote protocol is " + supportedRemotely.getProtocol() + "@" + supportedRemotely.getVersion());
        }
    }

    private static boolean equalWithoutRecursion(SupportedProtocols a, SupportedProtocols b) {
        return a.getProtocol().equals(b.getProtocol()) && a.getVersion().equals(b.getVersion());
    }

    private static SupportedProtocols[] buildCommonDenominator(List<SupportedProtocols> localSubs, List<SupportedProtocols> remoteSubs) {
        List<SupportedProtocols> common = new LinkedList<>();
        for (SupportedProtocols localSub : localSubs) {
            for (SupportedProtocols remoteSub : remoteSubs) {
                if (equalWithoutRecursion(localSub, remoteSub)) {
                    common.add(new SupportedProtocols(localSub.getProtocol(), localSub.getVersion(), buildCommonDenominator(localSub.getSubProtocols(), remoteSub.getSubProtocols())));
                }
            }
        }

        SupportedProtocols[] retrValue = new SupportedProtocols[common.size()];
        for (int i = 0; i < retrValue.length; i++) {
            retrValue[i] = common.get(i);
        }
        return retrValue;
    }

}
