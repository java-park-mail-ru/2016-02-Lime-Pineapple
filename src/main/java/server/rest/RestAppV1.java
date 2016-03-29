package server.rest;

import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;



// JERSEY ignores ApplicationPath


@ApplicationPath("/api/v1/")
public class RestAppV1 extends Application {
    private static final Logger LOGGER = LogManager.getLogger(RestAppV1.class);
    @Override
    public Set<Object> getSingletons() {

        LOGGER.error("[+] Started application...");

        final HashSet<Object> objects = new HashSet<>();
        final AccountService accountService = new ExampleAccountService();
        objects.add(new UserServlet(accountService));
        objects.add(new SessionServlet(accountService));

        return objects;
    }
}
