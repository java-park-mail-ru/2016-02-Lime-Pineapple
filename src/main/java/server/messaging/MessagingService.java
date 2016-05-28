package server.messaging;

import game.GameRoom;
import game.PlayingUser;
import org.jetbrains.annotations.NotNull;
import server.messaging.socket.MessagingSocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * created: 5/26/2016
 * package: server.messaging
 */
public class MessagingService {
    Map<Integer, MessagingSocket> idToSockets = new ConcurrentHashMap<>();
    Map<PlayingUser, MessagingSocket> usersToSockets = new ConcurrentHashMap<>();
    Map<Long, GameRoom> runningGames =new ConcurrentHashMap<>();

    Map<MessageType, MessageCallback> callbackMapType = new ConcurrentHashMap<>();
    Map<String, MessageCallback> callbackMapName = new ConcurrentHashMap<>();

    public void subscribe(@NotNull MessageType type, @NotNull MessageCallback callback) {
        callbackMapType.put(type, callback);
    }

    public void subscribe(@NotNull String messageName, @NotNull MessageCallback callback) {
        callbackMapName.put(messageName, callback);
    }

    public void trigger(Message message) {
        // send to thread pool to process it

    }

}
