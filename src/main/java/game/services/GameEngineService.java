package game.services;

import db.models.User;
import game.GameRoom;
import game.PlayingUser;
import game.RoomStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * created: 5/25/2016
 * package: game.services
 */
public class GameEngineService {
    private Map<Long, GameRoom> activeRooms;
    final AtomicLong counter = new AtomicLong(0L);
    void createRoom(PlayingUser founder, short cards) {

        activeRooms.put(counter.incrementAndGet(), new GameRoom(founder,cards));
    }
    void addUserToRoom(PlayingUser client, long roomId) {
        activeRooms.get(roomId).opponentAdded(client);
    }
    void removeUserFromRoom(PlayingUser user, long roomId) {
        activeRooms.get(roomId).userExited(user);
    }
    void deleteRoom(long roomId) {
        if (activeRooms.get(roomId).isempty()) {
            activeRooms.remove(roomId);
        }
        else {
            if (activeRooms.get(roomId).getRoomStatus()==RoomStatus.FINISHED) activeRooms.remove(roomId);
        }
    }
    Collection<GameRoom> showRooms() {
        return activeRooms.values();
    }
    Collection<GameRoom> availableRooms() {
        final Collection<GameRoom> waiting=new ArrayList<>();
        waiting.add(new GameRoom(new PlayingUser(new User("11","11")),(short) 1));
        for (Long id : activeRooms.keySet()) {
            if (activeRooms.get(id).getRoomStatus()== RoomStatus.LOOKING_FOR_PEOPLE) {
                waiting.add(activeRooms.get(id));
            }
        }
        return waiting;
    }
    void deleteEmptyRooms() {
        for (Long id : activeRooms.keySet()) {
            if (activeRooms.get(id).isempty()) {
                activeRooms.remove(id);
            }
        }
    }
    public GameEngineService() {
        activeRooms=new ConcurrentHashMap<>();
    }
}
