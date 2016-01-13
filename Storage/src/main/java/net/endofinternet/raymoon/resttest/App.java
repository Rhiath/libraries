package net.endofinternet.raymoon.resttest;

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
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
//        sf.setResourceProvider(new SingletonResourceProvider(new CustomerService()));
        sf.setResourceProvider(new SingletonResourceProvider(new BusinessEntityService()));

        
        sf.setAddress("http://localhost:9000/");
        sf.setProvider(new AuthenticationHandler());
        //        AuthenticationHandler handler = new AuthenticationHandler();
        BindingFactoryManager manager = sf.getBus().getExtension(BindingFactoryManager.class);
        JAXRSBindingFactory factory = new JAXRSBindingFactory();
        factory.setBus(sf.getBus());
        manager.registerBindingFactory(JAXRSBindingFactory.JAXRS_BINDING_ID, factory);
        Server server = sf.create();
        
        System.out.println("started");
    }
}
