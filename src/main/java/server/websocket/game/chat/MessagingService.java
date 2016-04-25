package server.websocket.game.chat;

import javax.servlet.annotation.WebServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "MyEcho WebSocket Servlet (EXAMPLE)", urlPatterns = { "/sockets/echo" })
public class MessagingService extends WebSocketServlet {
    public static final long MAX_IDLE_TIMEOUT = 100 * 1000;
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(MessagingService.MAX_IDLE_TIMEOUT);
        factory.register(MessagingService.class);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response echoGet() {
        return Response.status(Response.Status.OK).build();
    }


}
