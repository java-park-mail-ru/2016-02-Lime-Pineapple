package server.rest;

import db.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import server.messaging.socket.MessagingServlet;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1/")
public class RestAppV1 extends ResourceConfig {
    private static final Logger LOGGER = LogManager.getLogger(RestAppV1.class);

    server.Context context;

    public RestAppV1(server.Context context) {
        LOGGER.info("[+] Started application...");
        this.context = context;
        final AccountService accountService = context.get(AccountService.class);
        register(new AbstractBinder(){
            @Override
            protected void configure() {
                bind(context).to(server.Context.class);
                bind(accountService).to(AccountService.class);
            }
        });
        register(UserServlet.class);
        register(SessionServlet.class);
    }


}
