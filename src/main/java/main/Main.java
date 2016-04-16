package main;

import db.services.AccountService;
import db.services.impl.ExampleAccountServiceImpl;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;//библиотека добавлена в libs
import org.glassfish.jersey.servlet.ServletContainer;
import server.rest.RestAppV1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.ws.rs.core.Application;
import java.util.EnumSet;

public class Main {
    static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    public static final int DEFAULT_PORT = 9999;
    public static final String DEFAULT_HOST = "http://localhost/local";

    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
        } else {
            port = DEFAULT_PORT;
            // TODO: Configure LOGGER and change back to info
            LOGGER.debug(String.format("Port is not specified. Default port - %d is used.", DEFAULT_PORT));
        }

        LOGGER.debug(String.format("Starting at port: %d", port));//теперь выводит использованный порт а не дефолтный

        final Server srv = new Server(port);

        // @see ContextHandler
        // this thing is basically instatiates all Servlets and ServletHandlers
        // We use this for Sessions, additional HEADER param in response (see below)
        // new ContextHandler (servlet initializer)
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        // new AccountService - User info storage and handler
        final AccountService accountService = new ExampleAccountServiceImpl();
        // @see Cross-Origin Resource Sharing (CORS)
        // This thing is needed to inject header into response
        // It behaves like Middleware between Servlets and response handlers
        FilterHolder cors = contextHandler.addFilter(CrossOriginFilter.class,"/api/*", EnumSet.of(DispatcherType.REQUEST));
        // We can manually set all hosts, to which we respond with such a header
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "localhost");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,PUT,DELETE,OPTIONS");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        // TODO: create SessionService - User sessions (active and/or authorized)
        // TODO: create GameSessionService - currently opened games and their states
        // TODO: create GameMechanicsService - GameLogical unit
        // TODO: create AntiFraudGameMechanicsService - Anti-Fraud system

        // new ServletHolder - container, which invokes/manages servlets
        final ServletHolder api_v1Holder = new ServletHolder(ServletContainer.class);
        LOGGER.error(Application.class.getName());

        server.Context restContext = new server.Context();
        restContext.put(db.services.AccountService.class , new db.services.impl.ExampleAccountServiceImpl());


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

        srv.setHandler(handlers);

        try
        {
            srv.start();
            // uncomment this to see current Server dump (used modules, stats, etc)
            // srv.dump(System.err);
            srv.join();
        }
        catch (Throwable t)
        {
            // show
            t.printStackTrace(System.err);
        }
    }

}