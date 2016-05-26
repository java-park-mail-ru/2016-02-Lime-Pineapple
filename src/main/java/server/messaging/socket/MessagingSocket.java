package server.messaging.socket;

import game.PlayingUser;
import game.services.MessagingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import server.Context;
import server.messaging.Client;

import java.io.IOException;

/**
 * created: 5/25/2016
 * package: server.messaging
 */
public class MessagingSocket extends WebSocketAdapter {
    private static final Logger LOGGER = LogManager.getLogger();

    final MessagingService service;
    final Client client;

    public MessagingSocket(MessagingService service, Client client) {
        this.service = service;
        this.client = client;
    }


    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        LOGGER.info("[!] Websocket connected");
        try {
            sess.getRemote().sendString("Hi, mazafaka");
        } catch (IOException e) {
            LOGGER.error(String.format("[ E ] Failed to send message: \n%s", e.toString()));
        }
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        LOGGER.error(String.format("[!] Websocket - error happened:\n%s", cause.toString()));
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        LOGGER.info("[!] Websocket sent binary payload!");
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOGGER.info("[!] Websocket disconnected!");
    }

    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        if ( this.isConnected() ) {
            try
            {
                LOGGER.info(String.format("Echoing back message [%s]%n", message));
                // echo the message back
                getRemote().sendString(message);
            }
            catch (IOException e)
            {
                LOGGER.error(String.format("[ E ] Failed to send message: %n%s", e.toString()));
            }
        }

    }
}
