/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.topologyCoordination;

import java.util.List;
import javax.jws.WebService;

/**
 *
 * @author raymoon
 */
@WebService
public interface TopologyCoordinationService {
    public void informAboutState(String sourceNode, List<LinkAvailability> availability);
    public String getNodeID();
}
