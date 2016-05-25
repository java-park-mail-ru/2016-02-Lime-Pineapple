package main;

import db.services.AccountService;
import db.services.impl.DBAccountServiceImpl;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jetbrains.annotations.NotNull;
import server.Context;
import server.rest.RestAppV1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import server.websocket.MessagingServlet;

import javax.ws.rs.core.Application;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import static java.lang.Integer.parseInt;


public class Main {
    static final Logger LOGGER = LogManager.getLogger();

    public static final int DEFAULT_PORT = 9999;
    public static final String DEFAULT_PROP_PATH = "cfg/server.properties";

    @NotNull
    private static Properties loadProperties() throws IOException {
        LOGGER.info(String.format("[ I ] Loading properties from path %s", DEFAULT_PROP_PATH));
        FileInputStream in = null;
        Properties props = null;
        IOException savedThrowable = null;
        try {
            in = new FileInputStream(DEFAULT_PROP_PATH);
            props = new Properties();
            props.load(in);
        } catch (IOException t) {
            LOGGER.error(String.format("[ E ] Something happened during configuration load:\n%s", t.toString()));
            savedThrowable = t;
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException ignored) {}
        }
        if (props == null) {
            // rethrow exception
            throw savedThrowable;
        }
        LOGGER.info("[ I ] Loaded properties successfully!");
        return props;
    }

    public static void main(String[] args) throws Exception {
        //final Configuration srvConfig=new Configuration("fdvdf");
        final Properties serverProps = loadProperties();
        final String address = serverProps.getProperty("server.address");
        final String portString = serverProps.getProperty("server.port");
        int port = 0;
        try {
            port = parseInt(portString);
        } catch (NumberFormatException ignored) {

        }
        if (port<=0) {
            port = DEFAULT_PORT;
            LOGGER.warn(String.format("[ W ] Port is not specified or is not valid. Default port - %d is used.", DEFAULT_PORT));
        }
        LOGGER.info("[ I ] Building server full endpoint address from configuration...");
        final InetSocketAddress addr = new InetSocketAddress(address, port);
        LOGGER.info("[ I ] Built successfully completed!");
        LOGGER.info(String.format("Starting by address: http://%s:%d", address,port));
        final Server srv = new Server(addr);
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        contextHandler.setContextPath("/");

        final Context restContext = new Context();
        restContext.put(AccountService.class , new DBAccountServiceImpl());
        restContext.put(MessagingServlet.class, new MessagingServlet());
        //add our Context to ServletContextHandler
        contextHandler.setAttribute("context",restContext);

        servletHolder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());

        contextHandler.addServlet(servletHolder,"/api/v1/*");

        // add websocket servlet
        final ServletHolder holderSockets = new ServletHolder("ws-events", MessagingServlet.class);
        contextHandler.addServlet(holderSockets, "/sockets/*");

        // Static resource servlet
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("static");


        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});
        srv.setHandler(handlers);

        try
        {
            srv.start();
            // uncomment this to see current Server dump (used modules, stats, etc)
            // srv.dump(System.err);
            srv.join();
        }
        catch (InterruptedException t)
        {
            LOGGER.error(t.getStackTrace());
        }
    }

}