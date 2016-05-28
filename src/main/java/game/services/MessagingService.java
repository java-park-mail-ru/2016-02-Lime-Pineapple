package game.services;

import server.messaging.socket.MessagingSocket;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

/**
 * created: 5/25/2016
 * package: game.services
 */
public class MessagingService {
    final AtomicInteger autoIncrement = new AtomicInteger(0);
    Lock lock;
    Map<Integer, MessagingSocket> clients = new ConcurrentHashMap<>();

    public void addClient(MessagingSocket socket) {
        final Integer val = autoIncrement.incrementAndGet();
        clients.put(val, socket);

    }
    public void removeClient(MessagingSocket socket) {
        lock.lock();
        for(Map.Entry<Integer, MessagingSocket> p : clients.entrySet()) {
            if (p.getValue().equals(socket)) {
                clients.remove(p.getKey());
                break;
            }
        }
        lock.unlock();
    }

    @NotNull
    public MessagingSocket ClientById(Integer id) {
        return clients.get(id);
    }




}
