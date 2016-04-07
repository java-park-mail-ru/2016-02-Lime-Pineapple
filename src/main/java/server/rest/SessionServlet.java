package server.rest;

import com.google.gson.JsonObject;
import db.services.AccountService;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.rest.common.Utils;

@Path("/session")
public class SessionServlet extends HttpServlet {
    private static final String EMPTY_JSON = new JsonObject().toString();

    private AccountService accountService;
    private static final Logger LOGGER = LogManager.getLogger(SessionServlet.class);

    public SessionServlet(AccountService accountService) {
        this.accountService = accountService;
        LOGGER.info("[!] Initialized");
    }

    private Long getIdFromRequest(HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        return (Long)currentSession.getAttribute(Utils.USER_ID_KEY);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSession(@Context HttpHeaders headers, @Context HttpServletRequest request) {
        final Long uid = getIdFromRequest(request);
        if (uid == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            final User realUser = this.accountService.getUser(uid);
            if (realUser == null) {
                return Response.status(Response.Status.OK).entity("User not found").build();
            } else {
                final JsonObject idJs = new JsonObject();
                idJs.addProperty("id", 1L);
                return Response.status(Response.Status.OK).entity(idJs.toString()).build();
            }
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSession(User requestedUser, @Context HttpServletRequest request) {
        final User realUser = accountService.getUser(requestedUser.getLogin());
        if (realUser == null || !realUser.getPassword().equals(requestedUser.getPassword())) {
            LOGGER.info("[!] Invalid logging "+requestedUser.getLogin());
            return Response.status(Response.Status.BAD_REQUEST).entity(EMPTY_JSON).build();
        } else {
            final HttpSession currentSession = request.getSession();
            currentSession.setAttribute(Utils.USER_ID_KEY, realUser.getId());
            final JsonObject idJs = new JsonObject();
            idJs.addProperty("id", 1L);
            return Response.status(Response.Status.OK).entity(idJs.toString()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSession(@Context HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        currentSession.removeAttribute(Utils.USER_ID_KEY);
        return Response.status(Response.Status.OK).entity(EMPTY_JSON).build();
    }

}
