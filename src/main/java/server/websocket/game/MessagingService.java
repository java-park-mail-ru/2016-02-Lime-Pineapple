package server.websocket.game;

import javax.servlet.annotation.WebServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * created: 12-Mar-16
 * package: main.game
 */

@SuppressWarnings("serial")
@WebServlet(name = "MyEcho WebSocket Servlet (EXAMPLE)", urlPatterns = { "/sockets/echo" })
public class MessagingService extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(100*1000);
        factory.register(MessagingService.class);
    }


}
