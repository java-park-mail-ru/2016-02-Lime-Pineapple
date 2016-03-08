package server.rest;

import db.services.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
/**
 * Created by e.shubin on 25.02.2016.
 */
// JERSEY ignores ApplicationPath
@ApplicationPath("/api/v1/")
public class RestAppV1 extends Application {
    private final static Logger logger = LogManager.getLogger(RestAppV1.class);
    @Override
    public Set<Object> getSingletons() {
        logger.error("[+] Started application...");
        final HashSet<Object> objects = new HashSet<>();
        AccountService accountService = new db.services.impl.ExampleAccountService();
        objects.add(new UserServlet(accountService));
        objects.add(new SessionServlet(accountService));
        //objects.add(new SignInServlet(accountService));
        return objects;
    }
}
