package server.rest;

import db.services.AccountService;
import db.services.impl.ExampleAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


<<<<<<< HEAD
// JERSEY ignores ApplicationPath
=======
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
@ApplicationPath("/api/v1/")
public class RestAppV1 extends Application {
    private static final Logger LOGGER = LogManager.getLogger(RestAppV1.class);
    @Override
    public Set<Object> getSingletons() {
<<<<<<< HEAD
        logger.info("[+] Started application...");
=======
        LOGGER.error("[+] Started application...");
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
        final HashSet<Object> objects = new HashSet<>();
        final AccountService accountService = new ExampleAccountService();
        objects.add(new UserServlet(accountService));
        objects.add(new SessionServlet(accountService));

        return objects;
    }
}
