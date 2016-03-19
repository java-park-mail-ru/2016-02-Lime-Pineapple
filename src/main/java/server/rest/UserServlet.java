package server.rest;

import db.models.game.ScoreTable;
import db.services.AccountService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.lang.String;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Singleton
@Path("/user/")
public class UserServlet extends HttpServlet {
    private AccountService accountService;
    private static final Logger LOGGER = LogManager.getLogger(UserServlet.class.toString());

    public UserServlet(AccountService accountService) {
        this.accountService = accountService;
        LOGGER.error("Initialized");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        LOGGER.error("[*] Getting users...");
        final Collection<User> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new User[allUsers.size()])).build();
    }

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByName(@PathParam("name") String name) {
        final User user = accountService.getUser(name);
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            return Response.status(Response.Status.OK).entity(user).build();
        }
    }

    @GET
    @Path("/totalScores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScoreTable() {
        logger.error("[*] Getting scoreboard...");
        final Collection<ScoreTable> allscores = accountService.getuserScores();
        return Response.status(Response.Status.OK).entity(allscores.toArray(new ScoreTable[allscores.size()])).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        if (accountService.addUser(user) == 0L) {
            return Response.status(Response.Status.OK).entity(user.getLogin()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
}
