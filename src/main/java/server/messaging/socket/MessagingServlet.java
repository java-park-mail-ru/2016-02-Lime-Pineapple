package server.messaging.socket;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


/**
 * created: 12-Mar-16
 * package: main.game
 */


/**
 * @see "https://habrahabr.ru/post/128380/"
 */
@SuppressWarnings("serial")
@WebServlet(name = "WebSocket Messaging Servlet", urlPatterns = { "/socket" })
public class MessagingServlet extends WebSocketServlet {
    private static final long MAX_IDLE_TIMEOUT = 100 * 1000;
    private static final Logger LOGGER = LogManager.getLogger(MessagingServlet.class);

    @Context
    ServletContext servletContext;

    @Override
    public void configure(WebSocketServletFactory factory) {
        LOGGER.info("[ I ] in configure()...");
        final server.Context context = (server.Context)servletContext.getAttribute(server.Context.CONTEXT_KEY);
        factory.getPolicy().setIdleTimeout(MessagingServlet.MAX_IDLE_TIMEOUT);
        factory.setCreator(new MessagingSocketCreator(context));
    }
}
