package main;

import db.services.AccountService;
import db.services.impl.DBAccountServiceImpl;
//import db.services.impl.ExampleAccountServiceImpl;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
//import org.eclipse.jetty.servlet.FilterHolder;
//import org.eclipse.jetty.servlets.CrossOriginFilter;//библиотека добавлена в libs
import org.glassfish.jersey.servlet.ServletContainer;
import server.Configuration;
import server.Context;
import server.rest.RestAppV1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import server.websocket.game.MessagingService;

//import javax.servlet.DispatcherType;
import javax.ws.rs.core.Application;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.EnumSet;


public class Main {
    static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    public static final int DEFAULT_PORT = 9999;
    private static Configuration srvConfig;
    protected static void readFromFile() {
        try(final FileInputStream file = new FileInputStream("cfg/dbConfig.xml")) {
            try (final XMLDecoder decode = new XMLDecoder(new BufferedInputStream(file))) {
                srvConfig=(Configuration) decode.readObject();
            }
            file.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        //final Configuration srvConfig=new Configuration("fdvdf");
        readFromFile();
        int port=srvConfig.getServerPort();
        if (port==0) {
            port = DEFAULT_PORT;
            LOGGER.info(String.format("Port is not specified. Default port - %d is used.", DEFAULT_PORT));
            // TODO: Configure LOGGER and change back to info
        }
        LOGGER.info(String.format("Starting at port: %d", port));
        final Server srv = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        // @see ContextHandler
        // this thing is basically instatiates all Servlets and ServletHandlers
        // We use this for Sessions, additional HEADER param in response (see below)
        // new ContextHandler (servlet initializer)
        contextHandler.setContextPath("/");
        LOGGER.info(Application.class.getName());

        final Context restContext = new Context();
        restContext.put(AccountService.class , new DBAccountServiceImpl());
        //add our Context to ServletContextHandler
        contextHandler.setAttribute("context",restContext);

        servletHolder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());
        // add holder to contextHandler
        contextHandler.addServlet(servletHolder,"/api/v1/*");
        final ServletHolder websocketHolder=new ServletHolder(ServletContainer.class);

        //contextHandler.addServlet(new ServletHolder(new MessagingService()), "/socket/echo/*");


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
            t.printStackTrace(System.err);
        }
    }

}