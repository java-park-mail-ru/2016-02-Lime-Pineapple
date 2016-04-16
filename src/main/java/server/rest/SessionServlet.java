package server.rest;

import com.google.gson.JsonObject;
import db.services.AccountService;

import javax.inject.Singleton;
//import javax.json.Json;
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


@Singleton
@Path("/session")
public class SessionServlet extends HttpServlet {

    private AccountService accountService;
    private final static Logger logger = LogManager.getLogger(SessionServlet.class);

    public SessionServlet(AccountService accountService) {
        this.accountService = accountService;
        logger.info("[!] Initialized");
    }

    private Long getIdFromRequest(HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        return (Long)currentSession.getAttribute(Utils.USER_ID_KEY);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSession(@Context HttpHeaders headers, @Context HttpServletRequest request) {
        Long uid = getIdFromRequest(request);
        User realUser = this.accountService.getUser(uid);
        if ( realUser == null ) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.EMPTY_JSON).build();
        } else {
            JsonObject idJs = new JsonObject();
            idJs.addProperty("id", 1L);
            return Response.status(Response.Status.OK).entity(idJs.toString()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSession(User requestedUser, @Context HttpServletRequest request) {
        final User realUser = accountService.getUser(requestedUser.getLogin());
        if (realUser == null || !realUser.getPassword().equals(requestedUser.getPassword())) {
            logger.info("[!] Invalid logging "+requestedUser.getLogin());
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.EMPTY_JSON).build();
        } else {
            HttpSession currentSession = request.getSession();
            currentSession.setAttribute(Utils.USER_ID_KEY, realUser.getId());
            JsonObject idJs = new JsonObject();
            idJs.addProperty("id", realUser.getId());
            return Response.status(Response.Status.OK).entity(idJs.toString()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSession(@Context HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        currentSession.removeAttribute(Utils.USER_ID_KEY);

        return Response.status(Response.Status.OK).entity(Utils.EMPTY_JSON).build();
    }

}
