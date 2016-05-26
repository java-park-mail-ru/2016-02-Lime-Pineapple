package server.messaging.socket;

/**
 * created: 5/25/2016
 * package: server.messaging
 */
import game.services.MessagingService;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;
import server.Context;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Properties;

public class MessagingSocketCreator implements WebSocketCreator
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final MessagingService service;
    private final Boolean cookieAuthorization;

    public MessagingSocketCreator(Context context)
    {
        // Create the reusable sockets
        this.service = context.get(MessagingService.class);
        final Properties props = context.get(Properties.class);
        this.cookieAuthorization = (Boolean)props.getOrDefault("websockets.cookieAuthorization", true);
    }

    protected Pair<Boolean, Long> isAuthorized(ServletUpgradeRequest req) {
        LOGGER.info(String.format("Websocket neeeds to be authorized: %b", this.cookieAuthorization));
        if (!this.cookieAuthorization)
            return new Pair<>(true, 0L);
        final List<HttpCookie> cookies = req.getCookies();
        LOGGER.info("[ I ] Getting authorization info");
        for (HttpCookie cookie : cookies) {
            // TODO: "id" label should be in settings or context
            if ( cookie.getName().equals("JAVA")) {
                final String value = cookie.getValue();
                try {
                    final Long id = Long.decode(value);
                    if (id > 0) {
                        LOGGER.info("[ I ] WebSocket authorized");
                        return new Pair<>(true, id);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warn("[ I ] Error when trying to convert user id from cookie");
                }
            }
        }
        LOGGER.info("[ I ] WebSocket not authorized.");
        return new Pair<>(false, 0L);
    }

    @Override
    @Nullable
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp)
    {
        final Pair<Boolean, Long> authInfo = isAuthorized(req);
        if (authInfo.getKey()) {
            for (String subprotocol : req.getSubProtocols())
            {
                if ("text".equals(subprotocol))
                {
                    resp.setAcceptedSubProtocol(subprotocol);

                    //return new MessagingSocket(service, authInfo.getValue());
                }
            }

            // No valid subprotocol in request, ignore the request
            return null;
        }
        else {
            try {
                resp.sendForbidden("Not authorized");
            } catch (IOException e) {
                LOGGER.warn(String.format("[E] ERROR while sending Forbidden response%n%s", e.toString()));
            }
            return null;
        }
    }
}
