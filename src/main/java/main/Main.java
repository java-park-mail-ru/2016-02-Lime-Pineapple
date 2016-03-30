package main;


import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
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
        final int port;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
        } else {
            port = DEFAULT_PORT;
            LOGGER.debug(String.format("Port is not specified. Default port - %d is used.", DEFAULT_PORT));
        }
        LOGGER.debug(String.format("Starting at port: %d", port));
        final Server srv = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");
        final FilterHolder cors = contextHandler.addFilter(CrossOriginFilter.class,"/api/*", EnumSet.of(DispatcherType.REQUEST));

       /// cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
       // cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "localhost");
       // cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD,PUT,DELETE,OPTIONS");
       // cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        final ServletHolder apiV1Holder = new ServletHolder(ServletContainer.class);
        LOGGER.error(Application.class.getName());
        apiV1Holder.setInitParameter("javax.ws.rs.Application",RestAppV1.class.getCanonicalName());
        contextHandler.addServlet(apiV1Holder,"/api/v1/*");
        //final AccountService accountService = new ExampleAccountService();
        //apiV1Holder.start();
        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("static");
        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, contextHandler});
        srv.setHandler(handlers);
        try
        {
            srv.start();
            srv.join();
        }
        catch (InterruptedException t)
        {
            t.printStackTrace(System.err);
        }
    }

}