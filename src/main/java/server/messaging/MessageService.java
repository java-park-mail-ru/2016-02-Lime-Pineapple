package server.messaging;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import game.GameRoom;
import game.PlayingUser;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import server.messaging.socket.MessageSocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * created: 5/26/2016
 * package: server.messaging
 */
public class MessageService {
    static final Logger LOGGER = LogManager.getLogger();

    final AtomicLong autoIncrement = new AtomicLong(0);
    final Map<Long, Client> idToClients = new ConcurrentHashMap<>();
    final Map<Client, MessageSocket> clientToSockets = new ConcurrentHashMap<>();

    // games that actually running
    final List<GameRoom> activeRooms = Collections.synchronizedList(new ArrayList<>());

    //callbacks
    final Map<MessageType, List<MessageCallback>> callbackMapType = new ConcurrentHashMap<>();
    final Map<String, List<MessageCallback>> callbackMapName = new ConcurrentHashMap<>();

    protected <T> void subscribeUntyped (Map<T, List<MessageCallback>> map, T value,  MessageCallback callback) {
        List<MessageCallback> callbackList = map.getOrDefault(value, null);
        if (callbackList == null) {
            callbackList = new ArrayList<>();
            map.put(value, callbackList);
        }
        callbackList.add(callback);
    }

    // Messaging INPUT

    public synchronized void subscribe(@NotNull MessageType type, @NotNull MessageCallback callback) {
        subscribeUntyped(this.callbackMapType, type, callback);
    }

    public synchronized void subscribe(@NotNull String messageName, @NotNull MessageCallback callback) {
        subscribeUntyped(this.callbackMapName, messageName, callback);
    }

    // Messaging OUTPUT

    public void trigger(Client sender, Message message) {
        // send to thread pool to process it
        final List<MessageCallback> namedCallbacks = this.callbackMapName.getOrDefault(message.getName(), null);
        final List<MessageCallback> typedCallbacks = this.callbackMapType.getOrDefault(message.getType(), null);
        if (namedCallbacks != null) {
            for(MessageCallback callback : namedCallbacks)
                new CallbackFiber(callback, sender, message).start();
        }
        if (typedCallbacks != null) {
            for(MessageCallback callback : typedCallbacks)
                new CallbackFiber(callback, sender, message).start();
        }
    }

    public void sendMessage(Client receiver, Message message) throws IOException {
        final MessageSocket socket = clientToSockets.get(receiver);
        socket.sendMessage(message);
    }

    public void sendMessage(GameRoom receiver, Message message) throws IOException {
        for (PlayingUser user : receiver.getUsers()) {
            try {
                final MessageSocket socket = getSocket(user);
                socket.sendMessage(message);
            } catch (Throwable t) {
                LOGGER.warn(String.format("[ W ] Unusual warning: exception during getting client by PlayingUser. Exception: %n%s", t.toString()) );
            }
        }

    }


    // CLIENT AND ROOMS MANAGEMENT

    protected void removeRoom(@NotNull GameRoom room) {
        this.activeRooms.remove(room);
    }

    public void addClient(@NotNull Client client, @NotNull MessageSocket socket) {
        final Long val = this.autoIncrement.incrementAndGet();
        client.setClientId(val);
        this.idToClients.put(val, client);
        this.clientToSockets.put(client, socket);
    }

    public void removeClient(@NotNull  Client client) {
        this.idToClients.remove(client.getClientId());
        this.clientToSockets.remove(client);
        try {
            final GameRoom room = getRoom(client);
            room.removeUser(client.getUser());
            // если комната пуста - удаляем её. Теперь она не нужна
            if (room.isEmpty()) {
                this.removeRoom(room);
            }
        } catch (NotFoundException ignored) {

        }
    }

    public void addActiveRoom(@NotNull GameRoom room) {
        this.activeRooms.add(room);
    }

    // CLIENT AND ROOMS GETTERS
    @NotNull
    public Client getClient(@NotNull Long id) {
        return idToClients.get(id);
    }

    @NotNull
    protected MessageSocket getSocket(@NotNull PlayingUser user) throws NotFoundException {
        for (Map.Entry<Client, MessageSocket> client : this.clientToSockets.entrySet()) {
            if(Objects.equals(client.getKey().getUser(), user)) {
                return client.getValue();
            }
        }
        throw new NotFoundException("Could not find socket by given PlayingUser");
    }

    @NotNull
    public GameRoom getRoom(@NotNull Client client) throws NotFoundException {
        try {
            for (GameRoom room: this.activeRooms) {
                if (room.hasUser(client.getUser()))
                    return room;
            }
        }
        catch (Throwable t) {
            LOGGER.warn(String.format("[ W ] Exception during call: getRoom():%n%s", t.toString()));
        }
        LOGGER.warn("[ W ] Not found room for client. Is he playing?");
        throw new NotFoundException("Room not found");

    }

    // -- CallbackFiber - multithreaded callback handler

    protected static class CallbackFiber extends Fiber<Void> {
        private final MessageCallback callback;
        private final Client sender;
        private final Message message;

        public CallbackFiber(MessageCallback callback, Client sender, Message message) {
            this.callback = callback;
            this.sender = sender;
            this.message = message;
        }

        @Override
        protected Void run() throws SuspendExecution, InterruptedException {
            callback.callback(sender, message);
            return null;
        }
    }
}
