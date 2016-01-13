/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.endofinternet.raymoon.datanode;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import net.endofinternet.raymoon.topologyCoordination.LinkAvailability;
import net.endofinternet.raymoon.topologyCoordination.TopologyCoordinationService;

/**
 *
 * @author raymoon
 */
@WebService(endpointInterface = "net.endofinternet.raymoon.topologyCoordination.TopologyCoordinationService",
        serviceName = "TopologyCoordinationService")
public class TopologyNode implements TopologyCoordinationService {

    private final String selfName;
    private boolean keepRunning = true;
    private final List<TopologyCoordinationService> receivers = new LinkedList<TopologyCoordinationService>();
    private Map<LinkAvailability, Date> observedTopology = new HashMap<LinkAvailability, Date>();
    private long selfMessageCounter = 0; // start from the beginning

    public TopologyNode(String selfName) {
        this.selfName = selfName;
    }

    public void addReceiver(TopologyCoordinationService receiver) {
        receivers.add(receiver);
    }

    public void stop() {
        keepRunning = false;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (keepRunning()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("here is a link loss?");
                    }

                    notifyAllReceivers();
                }
            }
        }.start();
    }

    private boolean keepRunning() {
        return keepRunning;
    }

    private void notifyAllReceivers() {
//        System.out.println(this.selfName + " informs others about state");

        List<TopologyCoordinationService> receiversCopy = new LinkedList<TopologyCoordinationService>();

        synchronized (receivers) {
            receiversCopy.addAll(receivers);
        }

        this.selfMessageCounter++;
        for (TopologyCoordinationService receiver : receiversCopy) {
            try {
                String receiverID = receiver.getNodeID();
                try {
                    receiver.informAboutState(selfName, buildCurrentlyObservedTopology(receiverID));
                    remember(this.selfName, receiverID, true, getTTL(), this.selfMessageCounter, new Date());
                } catch (Exception ex) {
                    remember(this.selfName, receiverID, false, getTTL(), this.selfMessageCounter, new Date());
                }
            } catch (Exception ex) {
                System.out.println("endpoint '" + receiver + "' not accessible offline");
            }
        }
    }

    private List<LinkAvailability> buildCurrentlyObservedTopology(String destination) throws CloneNotSupportedException {
        List<LinkAvailability> retValue = new LinkedList<LinkAvailability>();

        synchronized (observedTopology) {
            for (Entry<LinkAvailability, Date> candidate : this.observedTopology.entrySet()) {
                LinkAvailability candidateKey = candidate.getKey();

                candidateKey.ttlOfState -= System.currentTimeMillis() - candidate.getValue().getTime();
                candidateKey.connectionEstablished &= candidateKey.ttlOfState > 0;

                if (!(candidateKey.source.equals(this.selfName) && (candidateKey.destination.equals(destination)))) {
                    retValue.add(candidateKey);
                }
            }
        }

        LinkAvailability linkState = new LinkAvailability();
        linkState.connectionEstablished = true;
        linkState.destination = destination;
        linkState.source = selfName;
        linkState.version = selfMessageCounter;
        linkState.ttlOfState = getTTL();
        retValue.add(linkState);

        return retValue;
    }

    private long getTTL() {
        return 5000;
    }

    private void remember(String source, String destination, boolean hasConnection, long ttl, long messageCounter, Date dateOfMessag) {
        synchronized (observedTopology) {
            List<LinkAvailability> localMatchingEntries = new LinkedList<LinkAvailability>();
            for (LinkAvailability candidate : this.observedTopology.keySet()) {
                if (source.equals(candidate.source) && destination.equals(candidate.destination)) {
                    localMatchingEntries.add(candidate);
                }
            }

            for (LinkAvailability state : localMatchingEntries) {
                this.observedTopology.remove(state);
            }

            LinkAvailability linkState = new LinkAvailability();
            linkState.connectionEstablished = hasConnection;
            linkState.destination = destination;
            linkState.source = source;
            linkState.version = messageCounter;
            linkState.ttlOfState = ttl;
            this.observedTopology.put(linkState, dateOfMessag);
        }
    }

    public String getName() {
        return this.selfName;
    }

    void printState() {
        System.out.println("state node '" + this.getName() + "'   messageCounter=" + this.selfMessageCounter);
        List<LinkAvailability> states = new LinkedList<LinkAvailability>();
        synchronized (observedTopology) {
            states.addAll(observedTopology.keySet());
        }

        Collections.sort(states, new Comparator<LinkAvailability>() {
            public int compare(LinkAvailability t, LinkAvailability t1) {
                int sourceComp = t.source.compareTo(t1.source);
                int destComp = t.destination.compareTo(t1.destination);

                if (sourceComp == 0) {
                    return destComp;
                }
                return sourceComp;
            }
        });

        for (LinkAvailability state : states) {
            System.out.println(state.source + " --> " + state.destination + " : " + state.connectionEstablished + " TTL=" + state.ttlOfState + "  messagCounter=" + state.version);
        }
    }

    public void informAboutState(String sourceNode, List<LinkAvailability> availability) {
        if (Math.random() < 0.1) {
//            throw new RuntimeException("simulated link loss");
        }
        for (LinkAvailability toCheck : availability) {
            if (toCheck.source.equals(selfName)) {
                if (toCheck.version > this.selfMessageCounter) { // use the messageID I sent in previous messages
                    this.selfMessageCounter = toCheck.version;
                }
            }

            if (!toCheck.source.equals(selfName)) {
                rememberIfTTLPermits(toCheck);
            }
        }
    }

    private void rememberIfTTLPermits(LinkAvailability toCheck) {
        List<LinkAvailability> localMatchingEntries = new LinkedList<LinkAvailability>();

        synchronized (observedTopology) {
            boolean rememberValue = true;
            for (LinkAvailability candidate : this.observedTopology.keySet()) {
                if (candidate.source.equals(toCheck.source) && candidate.destination.equals(toCheck.destination)) {
                    if (candidate.version > toCheck.version) {
//                        System.out.println(this.selfName + ": newer message counter for " + candidate.source + " --> " + candidate.destination + "  is known");
                        rememberValue = false;
                    } else {
                        localMatchingEntries.add(candidate);
                    }
                }
            }

            if (rememberValue) {
                for (LinkAvailability state : localMatchingEntries) {
                    this.observedTopology.remove(state);
                }
                this.observedTopology.put(toCheck, new Date()); // remember value
            }
        }
    }

    public String getNodeID() {
        return selfName;
    }
}
