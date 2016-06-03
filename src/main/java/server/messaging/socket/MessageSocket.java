package server.messaging.socket;

import server.messaging.Message;
import server.messaging.messages.SystemMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import server.messaging.Client;
import server.messaging.MessageService;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * created: 5/25/2016
 * package: server.messaging
 */
public class MessageSocket extends WebSocketAdapter {
    private static final Logger LOGGER = LogManager.getLogger();

    final MessageService service;
    final Client client;
    Session session = null;

    public MessageSocket(MessageService service, Client client) {
        this.service = service;
        this.client = client;
    }


    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        LOGGER.info("[ ! ] Websocket connected");
        this.session = this.getSession();
        this.service.addClient(this.client, this);
        // триггерим событие "Клиент подключен". Его может обработать MatchmakingService
        this.service.trigger(this.client, new SystemMessage(SystemMessage.MESSAGES.CLIENT_CONNECTED));

    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        LOGGER.warn(String.format("[ ! ] Websocket - error happened:\n%s", cause.toString()));
        // TODO: что делать после этого?
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOGGER.info("[ ! ] Websocket disconnected!");
        this.service.trigger(this.client, new SystemMessage(SystemMessage.MESSAGES.CLIENT_DISCONNECTED));
        // Удаляем клиента. Если клиент находился в комнате, и комната теперь пуста, эта комната удаляется.
        this.service.removeClient(this.client);
    }

    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        try {
            LOGGER.info(String.format("[ ! ] Got message: %s", message));
            this.service.trigger(this.client, Message.deserializeToGenericMessage(message));

        } catch (Throwable t) {
            LOGGER.error(String.format("[ W ] Could not serialize incoming message. Message is malformed? Exception: %n%s", t.toString()));
        }

    }

    // метод возвращается сразу же после начала отправки сообщения
    // отдаёт фьючер, с помощью которого можно дождаться настоящей отправки
    public Future<Void> sendMessage(Message message) throws IOException {
        // Делаем вызов отправки сообщения асинхронным
        if (this.isNotConnected()||!this.session.isOpen())
            throw new IOException("Could not write to closed WebSocket");
        return this.session.getRemote().sendStringByFuture(message.serialize());
    }

}
