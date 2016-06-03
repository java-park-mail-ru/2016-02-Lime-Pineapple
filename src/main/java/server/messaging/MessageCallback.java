package server.messaging;

/**
 * created: 5/26/2016
 * package: server.messaging
 */
public interface MessageCallback {
    void callback(Client sender, Message message);
}
