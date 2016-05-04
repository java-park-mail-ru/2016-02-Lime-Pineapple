package main;

import db.services.AccountService;
import db.services.impl.DBAccountServiceImpl;
import db.services.impl.ExampleAccountServiceImpl;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;//библиотека добавлена в libs
import org.glassfish.jersey.servlet.ServletContainer;
import server.Configuration;
import server.Context;
import server.rest.RestAppV1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.Application;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.EnumSet;


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
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        int port;
            // TODO: Configure LOGGER and change back to info
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
        // It behaves like Middleware between Servlets and response handlers
        //cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        //cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "localhost");
        //cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,PUT,DELETE,OPTIONS");
        //cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        // TODO: create SessionService - User sessions (active and/or authorized)
        // TODO: create GameSessionService - currently opened games and their states
        // TODO: create GameMechanicsService - GameLogical unit
        // TODO: create AntiFraudGameMechanicsService - Anti-Fraud system
        final ServletHolder api_v1Holder = new ServletHolder(ServletContainer.class);
        // @see ContextHandler
        // this thing is basically instatiates all Servlets and ServletHandlers
        // We use this for Sessions, additional HEADER param in response (see below)
        // new ContextHandler (servlet initializer)
        contextHandler.setContextPath("/");
        // new AccountService - User info storage and handler
        //final AccountService accountService = new ExampleAccountServiceImpl();
        // @see Cross-Origin Resource Sharing (CORS)
        // This thing is needed to inject header into response
        // It behaves like Middleware between Servlets and response handlers
        // We can manually set all hosts, to which we respond with such a header
        LOGGER.info(Application.class.getName());

        Context restContext = new Context();
        restContext.put(AccountService.class , new ExampleAccountServiceImpl());
        api_v1Holder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());
        // add holder to contextHandler
        contextHandler.addServlet(api_v1Holder,"/api/v1/*");

        // Static resource servlet
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");
        // Used to store handlers. Basically used as Handler[]
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, contextHandler});
        //contextHandler.setHandler(handlers);

        final Context restContext = new Context();
        restContext.put(AccountService.class , new DBAccountServiceImpl());


        //api_v1Holder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());
        // add holder to contextHandler
        //contextHandler.addServlet(api_v1Holder,"/api/v1/*");

        // Static resource servlet
        //final ResourceHandler resourceHandler = new ResourceHandler();
        //resourceHandler.setDirectoriesListed(true);
        //resource_handler.setResourceBase("static");
        // Used to store handlers. Basically used as Handler[]
        //handlers.setHandlers(new Handler[]{resource_handler, contextHandler});
        //contextHandler.setHandler(handlers);


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