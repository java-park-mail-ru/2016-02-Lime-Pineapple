package main;


import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import server.rest.RestAppV1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.ws.rs.core.Application;

/**
 * @author esin88
 */
public class Main {
    static final Logger logger = LogManager.getLogger(Main.class.getName());
    public static final int DEFAULT_PORT = 9999;
    public static final String DEFAULT_HOST = "http://localhost/local";

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
        } else {
            port = DEFAULT_PORT;
            // TODO: Configure logger and change back to info
            logger.error(String.format("Port is not specified. Default port - %d is used.", DEFAULT_PORT));
        }

        logger.error(String.format("Starting at port: %d",DEFAULT_PORT));

        final Server srv = new Server(port);

        // new ContextHandler (servlet initializer)
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        // new AccountService - User info storage and handler
        final AccountService accountService = new ExampleAccountService();
        // TODO: create SessionService - User sessions (active and/or authorized)
        // TODO: create GameSessionService - currently opened games and their states
        // TODO: create GameMechanicsService - GameLogical unit
        // TODO: create AntiFraudGameMechanicsService - Anti-Fraud system

        // new ServletHolder - container, which invokes/manages servlets
        final ServletHolder api_v1Holder = new ServletHolder(ServletContainer.class);
        logger.error(Application.class.getName());
        api_v1Holder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());
        // add holder to contextHandler
        contextHandler.addServlet(api_v1Holder,"/api/v1/*");

        // Static resource servlet and servlet handler bundle
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, contextHandler});
        //contextHandler.setHandler(handlers);

        srv.setHandler(handlers);

        try
        {
            srv.start();
            //srv.dump(System.err);
            srv.join();
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }

}