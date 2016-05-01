package server.rest;

import db.services.AccountService;
import db.services.impl.DBAccountServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1/")
public class RestAppV1 extends Application {
    private static final Logger LOGGER = LogManager.getLogger(RestAppV1.class);
    final HashSet<Object> objects;
    final AccountService accountService;
    @Context
    ServletContext ctx;
    public RestAppV1() {
        objects = new HashSet<>();
        accountService = new DBAccountServiceImpl();
        LOGGER.info("AccountService Initialized");
    }
//    @Override
//    public Set<Object> getSingletons() {
///      LOGGER.debug("[+] Started application...");
//        objects.add(new UserServlet(accountService));
//        objects.add(new SessionServlet(accountService));

    @Context
    ServletContext ctx;
    @Override
    public Set<Object> getSingletons() {
        LOGGER.info("[+] Started application...");
        //final HashSet<Object> objects = new HashSet<>();
        // TODO: change to context
        //final AccountService accountService = new ExampleAccountServiceImpl();
        objects.add(new UserServlet(accountService));
        objects.add(new SessionServlet(accountService));
        //objects.add(new SignInServlet(accountService));
        return objects;
    }
}
