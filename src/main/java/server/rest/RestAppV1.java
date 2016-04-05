package server.rest;

import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/api/v1/")
public class RestAppV1 extends Application {
    private static final Logger LOGGER = LogManager.getLogger(RestAppV1.class);
    final HashSet<Object> objects;
    final AccountService accountService;
    public RestAppV1() {
        objects = new HashSet<>();
        accountService = new ExampleAccountService();
        LOGGER.error("AccountService Initialized");
    }
    @Override
    public Set<Object> getSingletons() {
        LOGGER.error("[+] Started application...");
        objects.add(new UserServlet(accountService));
        objects.add(new SessionServlet(accountService));

        return objects;
    }
}
