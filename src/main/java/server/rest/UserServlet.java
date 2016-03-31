package server.rest;

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
    public Response getUserByName(@PathParam("name") Long name) {
        final User user = accountService.getUser(name);
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            return Response.status(Response.Status.OK).entity(user).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user) {
        if (accountService.addUser(user) != 0L) {
            return Response.status(Response.Status.OK).entity(user.getLogin()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    @POST
    @Path("{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("name") Long name, User user) {
        if (accountService.changeUser(user)) {
            return Response.status(Response.Status.OK).entity(user.getId()).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@PathParam("name") Long username) {

        if (accountService.removeUser(username)) {
            return Response.status(Response.Status.OK).build();
        }
        else return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/totalScores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showScoreTable() {
        LOGGER.error("[*] Getting scoreboard...");
        final Collection<String> allscores = accountService.getuserScores();
        return Response.status(Response.Status.OK).entity(allscores.toString()).build();
    }
}
