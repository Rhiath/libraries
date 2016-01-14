package net.endofinternet.raymoon.resttest;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import net.endofinternet.raymoon.resttest.restServices.Registry;
import net.endofinternet.raymoon.resttest.restServices.RegistryImpl;
import net.endofinternet.raymoon.resttest.services.BusinessEntityService;
import org.apache.cxf.binding.BindingFactoryManager;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        Registry restRegistry = new RegistryImpl();
        startPersistence();
        //OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/petshop").open("admin", "admin");
        // restRegistry.register(BusinessEntityService.class, new BusinessEntityService(db));

        startRestServices(9000, restRegistry);

    }

    private static void startRestServices(int port, Registry restRegistry) {

        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();

        for (Class type : restRegistry.getRegisteredTypes()) {
            sf.setResourceProvider(new SingletonResourceProvider(restRegistry.getService(type)));
        }

        sf.setAddress("http://0.0.0.0:" + port + "/");
        addAuthenticationHandler(sf);
        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(sf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);
        final Server server = sf.create();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.stop();
            }
        });

        // System.out.println("started");
    }

    private static void addAuthenticationHandler(JAXRSServerFactoryBean sf) {
        sf.setProvider(new AuthenticationHandler());
    }

    private static OServer startPersistence() throws Exception {
        final OServer server = OServerMain.create();
        server.startup(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<orient-server>"
                + "<network>"
                + "<protocols>"
                + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
                + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
                + "</protocols>"
                + "<listeners>"
                + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
                + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
                + "</listeners>"
                + "</network>"
                + "<users>"
                + "<user name=\"root\" password=\"ThisIsA_TEST\" resources=\"*\"/>"
                + "</users>"
                + "<properties>"
                + "<entry name=\"orientdb.www.path\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/www/\"/>"
                + "<entry name=\"orientdb.config.file\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/config/orientdb-server-config.xml\"/>"
                + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
                + "<entry name=\"log.console.level\" value=\"info\"/>"
                + "<entry name=\"log.file.level\" value=\"fine\"/>"
                //The following is required to eliminate an error or warning "Error on resolving property: ORIENTDB_HOME"
                + "<entry name=\"plugin.dynamic\" value=\"false\"/>"
                + "</properties>" + "</orient-server>");
        server.activate();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.shutdown();
            }
        });

        return server;
    }
}
