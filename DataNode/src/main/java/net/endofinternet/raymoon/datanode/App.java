package net.endofinternet.raymoon.datanode;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.endofinternet.raymoon.topologyCoordination.TopologyCoordinationService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        List<TopologyNode> nodes = new LinkedList<TopologyNode>();
        for (int i = 0; i < 10; i++) {
            nodes.add(new TopologyNode("node#" + i));
        }

        for (int i = 0; i < 10; i++) {
            TopologyNode node = nodes.get(i);
            JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
            svrFactory.setServiceClass(TopologyCoordinationService.class);
            svrFactory.setAddress("http://localhost:" + (9000 + i) + "/topologyCoordinationService");
            svrFactory.setServiceBean(node);
//            svrFactory.getInInterceptors().add(new LoggingInInterceptor());
//            svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
            svrFactory.create();
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < 1; j++) {
                int otherID = i + 1 + j;
                getRemoteEndpoint(otherID);


                otherID = otherID % nodes.size();
                nodes.get(i).addReceiver(getRemoteEndpoint(otherID));
                otherID = i - 1 - j;
                otherID += nodes.size();
                otherID = otherID % nodes.size();
                nodes.get(i).addReceiver(getRemoteEndpoint(otherID));
            }
        }

        Random r = new Random();
        for (TopologyNode node : nodes) {
            Thread.sleep(r.nextInt(100) + 3);
            node.start();
        }
        Thread.sleep(r.nextInt(100) + 3);

        while (true) {
            int n = 0;
            Thread.sleep(1000);
            for (TopologyNode node : nodes) {
                n++;
                if (n == 1) {
                    node.printState();
                    System.out.println("");
                }
            }
        }
    }

    private static void showNodeScreen(TopologyNode node) {
        JFrame frame = new JFrame(node.getName());
        JPanel panel = new JPanel();
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    private static TopologyCoordinationService getRemoteEndpoint(int i) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.getInInterceptors().add(new LoggingInInterceptor());
//        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(TopologyCoordinationService.class);
        factory.setAddress("http://localhost:" + (9000 + i) + "/topologyCoordinationService");
        return (TopologyCoordinationService) factory.create();
    }
}
