package server.rest;

import com.google.gson.JsonObject;
import db.services.AccountService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import db.models.User;
import db.services.SessionService;
import db.services.impl.SessionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.rest.common.Utils;


@Path("/session")
public class SessionServlet extends HttpServlet implements Runnable {

    private AccountService accountService;
    private static final Logger LOGGER = LogManager.getLogger(SessionServlet.class);
    SessionService sessionList = new SessionServiceImpl();

    @Override
    public void run() {
        LOGGER.info("started");
    }
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
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            } else {
                final long sessId=sessionList.checkAuthorization(realUser);
                if (sessId!=0L) {
                    final JsonObject idJs = new JsonObject();
                    idJs.addProperty("id", sessId);
                    return Response.status(Response.Status.OK).entity(idJs.toString()).build();
                }
                else return Response.status(Response.Status.UNAUTHORIZED).entity("Not logged in").build();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSession(User requestedUser, @Context HttpServletRequest request) {
        final User realUser = accountService.getUser(requestedUser.getUsername());
        if (realUser == null || !realUser.getPassword().equals(requestedUser.getPassword())) {
            LOGGER.info("[!] Invalid logging "+requestedUser.getUsername());
            return Response.status(Response.Status.BAD_REQUEST).entity(Utils.EMPTY_JSON).build();
        } else {
            final HttpSession currentSession = request.getSession();
            currentSession.setAttribute(Utils.USER_ID_KEY, realUser.getId());
            final long sessId=sessionList.logIn(realUser);
            if (sessId!=0L) {
                final JsonObject idJs = new JsonObject();
                idJs.addProperty("id", sessId);
                return Response.status(Response.Status.OK).entity(idJs.toString()).build();
            }
            else return Response.status(Response.Status.BAD_REQUEST).entity(Utils.EMPTY_JSON).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSession(@Context HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        currentSession.removeAttribute(Utils.USER_ID_KEY);
        final Long uid = getIdFromRequest(request);
        final User realUser=accountService.getUser(uid);
        if (realUser!=null) {
            sessionList.logOut(realUser);
        }
        return Response.status(Response.Status.OK).entity(Utils.EMPTY_JSON).build();
    }
}