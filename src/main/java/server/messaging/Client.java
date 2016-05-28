package server.messaging;

import game.PlayingUser;

/**
 * created: 5/25/2016
 * package: server.messaging
 */
public class Client {
    private final PlayingUser user;
    private final int clientId;

    public Client(PlayingUser user, int clientId) {
        this.user = user;
        this.clientId = clientId;
    }

    public PlayingUser getUser() {
        return user;
    }

    public int getClientId() {
        return clientId;
    }
}
