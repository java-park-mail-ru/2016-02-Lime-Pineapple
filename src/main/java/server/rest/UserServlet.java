package server.rest;

import db.services.AccountService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import db.models.User;
import server.rest.common.Utils;


@Singleton
@Path("/user/")
public class UserServlet extends HttpServlet {
    private AccountService accountService;
    private final static Logger logger = LogManager.getLogger(UserServlet.class);

    public UserServlet(AccountService accountService) {
        this.accountService = accountService;
        logger.error("Initialized");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        logger.error("[*] Getting users...");
        final Collection<User> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new User[allUsers.size()])).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByName(@PathParam("id") Long id) {
        final User user = accountService.getUser(id);
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
        final Collection<String> allscores = accountService.getUserScores();
        return Response.status(Response.Status.OK).entity(allscores.toArray(new String[allscores.size()])).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user, @Context HttpHeaders headers) {
        if (accountService.addUser(user) == 0L) {
            return Response.status(Response.Status.OK).entity(user.getLogin()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(Utils.EMPTY_JSON).build();
        }
    }
}
