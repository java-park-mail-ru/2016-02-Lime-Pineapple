package main;

import db.services.AccountService;
import db.services.impl.db.DBAccountServiceImpl;
import db.services.impl.db.DBSessionFactoryService;
import game.services.MessagingService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.Context;
import server.rest.RestAppV1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import server.messaging.socket.MessagingServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import static java.lang.Integer.parseInt;


public class Main {
    static final Logger LOGGER = LogManager.getLogger();

    public static final int DEFAULT_PORT = 9999;
    public static final String DEFAULT_PROP_PATH = "cfg/server.properties";

    @NotNull
    static Properties loadProperties(Context serverContext) throws IOException {
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
        serverContext.put(Properties.class, props);
        return props;
    }



    @Nullable
    static Server initializeServer(Context serverContext) {
        //final Configuration srvConfig=new Configuration("fdvdf");
        final Properties serverProps;
        try {
            serverProps = loadProperties(serverContext);
        } catch (IOException e) {
            LOGGER.error(String.format("[ E ] Error when loading properties: %n%s", e.toString()));
            return null;
        }
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
        LOGGER.info(String.format("Address after configuration: http://%s:%d", address,port));
        return new Server(addr);
    }

    @NotNull
    static ServletContextHandler initializeContextHandler(Context restContext) {
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        contextHandler.setAttribute(Context.CONTEXT_KEY,restContext);
        return contextHandler;
    }

    @NotNull
    static ResourceHandler initializeResourceHandler() {
        // Static resource servlet
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("static");
        return resourceHandler;
    }

    static void configureRestApi(ServletContextHandler contextHandler) {
        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        //add our Context to ServletContextHandler
        servletHolder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());
        contextHandler.addServlet(servletHolder,"/api/v1/*");
    }



    static void configureAccountService(Context restContext) {
        final DBSessionFactoryService factory = new DBSessionFactoryService();
        factory.configure();
        final AccountService service = new DBAccountServiceImpl(factory);
        restContext.put(AccountService.class , service);
        restContext.put(DBSessionFactoryService.class, factory);
    }

    static void configureMessagingService(Context restContext, ServletContextHandler contextHandler) {
        restContext.put(MessagingService.class, new MessagingService());
        final ServletHolder holderSockets = new ServletHolder("ws-events", MessagingServlet.class);
        contextHandler.addServlet(holderSockets, "/sockets/*");
    }


    static void configureServer(Handler[] handlers, Server server) {
        final HandlerList handlerList = new HandlerList();
        handlerList.setHandlers(handlers);
        server.setHandler(handlerList);
    }

    public static void main(String[] args) throws Exception {

        final Context serverContext = new Context();
        final Server server = initializeServer(serverContext);
        if (server == null)
            return;

        final ResourceHandler resourceHandler = initializeResourceHandler();
        final ServletContextHandler contextHandler = initializeContextHandler(serverContext);

        configureAccountService(serverContext);
        configureMessagingService(serverContext, contextHandler);
        configureRestApi(contextHandler);

        configureServer(new Handler[]{resourceHandler, contextHandler}, server);

        try
        {
            LOGGER.info("[ I ] Starting server...");
            server.start();
            // uncomment this to see current Server dump (used modules, stats, etc)
            // srv.dump(System.err);
            server.join();
        }
        catch (InterruptedException t)
        {
            LOGGER.error(String.format("[ E ] Critical error in server work:%n%s", t.toString()));
        }
    }

}