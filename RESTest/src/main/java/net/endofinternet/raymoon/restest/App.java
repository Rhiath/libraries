package net.endofinternet.raymoon.restest;

import com.mkyong.app.MessageApplication;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        final QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(2); // 10
        threadPool.setMaxThreads(8); // 200
        threadPool.setDetailedDump(false);
        threadPool.setName("SERVER THREAD POOL");
        threadPool.setDaemon(true);

        final SelectChannelConnector connector = new SelectChannelConnector();
        connector.setHost("0.0.0.0");
        connector.setAcceptors(2);
        connector.setPort(8192);
        connector.setMaxIdleTime(5000);
        connector.setStatsOn(true);
        connector.setLowResourcesConnections(2);
        connector.setLowResourcesMaxIdleTime(3000);
        connector.setName("MY CONNECTOR");

        /* Setup ServletContextHandler */
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
//        contextHandler.addEventListener(new ProxyContextListener());

        contextHandler.setInitParameter("resteasy.servlet.mapping.prefix", "/");

        final ServletHolder restEasyServletHolder = new ServletHolder(new DefaultServlet() );
        restEasyServletHolder.setInitOrder(1);

        /* Scan package for web services*/
        restEasyServletHolder.setInitParameter("javax.ws.rs.Application", MessageApplication.class.getCanonicalName());

        contextHandler.addServlet(restEasyServletHolder, "/services");

        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{contextHandler});

        final Server server = new Server();
        server.setThreadPool(threadPool);
        server.setConnectors(new Connector[]{connector});
        server.setHandler(handlers);
        server.setStopAtShutdown(true);
        server.setSendServerVersion(true);
        server.setSendDateHeader(true);
        server.setGracefulShutdown(1000);
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);

        server.start();
        System.out.println("service ready");
        server.join();
    }
}
