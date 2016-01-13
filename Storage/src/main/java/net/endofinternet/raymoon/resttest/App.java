package net.endofinternet.raymoon.resttest;

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

    public static void main(String[] args) {
        Registry restRegistry = new RegistryImpl();

        restRegistry.register(BusinessEntityService.class, new BusinessEntityService());

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
        Server server = sf.create();

        // System.out.println("started");
    }

    private static void addAuthenticationHandler(JAXRSServerFactoryBean sf) {
        sf.setProvider(new AuthenticationHandler());
    }
}
