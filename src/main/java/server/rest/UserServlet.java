package server.rest;

import db.services.AccountService;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.AccessException;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static server.rest.common.Utils.EMPTY_JSON;



import db.models.User;


@Path("/user/")
public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(UserServlet.class);
    private AccountService accountService;

    public UserServlet(AccountService accountService) {
        this.accountService = accountService;
        LOGGER.debug("Initialized");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() throws AccessException {

        LOGGER.debug("[*] Getting users...");
        final Collection<User> allUsers = accountService.getUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new User[allUsers.size()])).build();
    }
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) throws AccessException {
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
        LOGGER.error("[*] Getting scoreboard...");
        //final Collection<String> allscores = accountService.getUserScores();
        return Response.status(Response.Status.OK).entity(EMPTY_JSON).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user, @Context HttpHeaders headers) {
        try {
            if (accountService.addUser(user) != 0) {
                return Response.status(Response.Status.OK).entity(user).build();
            }
            else {
                return Response.status(Response.Status.FORBIDDEN).entity(EMPTY_JSON).build();
            }
        } catch (AccessException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
        }
    }
    @POST
    @Path("{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("name") Long name, User user) {
        if (accountService.changeUser(user)) {
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity(EMPTY_JSON).build();
        }
    }

    @DELETE
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@PathParam("name") Long username) {

        try {
            if (accountService.removeUser(username)) {
                return Response.status(Response.Status.OK).entity(EMPTY_JSON).build();
            }
            else return Response.status(Response.Status.FORBIDDEN).build();
        } catch (AccessException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
        }
    }


}
