package game.services;

import db.models.User;
import game.GameRoom;
import game.PlayingUser;
import game.RoomStatus;
import org.jetbrains.annotations.Nullable;

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
    final AtomicLong userCounter=new AtomicLong(0L);
    Map<Long, PlayingUser> activeUsers;
    void createRoom(PlayingUser founder, short cards) {
        final long newRoomId=counter.incrementAndGet();
        founder.setRoom(newRoomId);
        activeRooms.put(newRoomId, new GameRoom(founder,cards));
        activeRooms.get(newRoomId).setId(newRoomId);
    }
    void addUserToRoom(PlayingUser client, long roomId) {
        client.setRoom(roomId);
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
        activeUsers=new ConcurrentHashMap<>();
    }
    public void cardPlayed(Long roomID) {

    }
    public void userLogin(User user) {
        activeUsers.put(userCounter.incrementAndGet(),new PlayingUser(user));
    }
    public long getRoomIdByUser(PlayingUser user) {
        return user.getCurrentRoom();
    }
    public void userLogout(User user) {
        for (Long id : activeUsers.keySet()) {
            if (activeUsers.get(id).getName().equals(user.getUsername())) {
                final PlayingUser logoutUser=activeUsers.get(id);
                userExitedRoom(logoutUser);
                activeUsers.remove(id);
            }
        }
    }
    @Nullable
    public PlayingUser getUserOpponent(PlayingUser user) {
        if (user.getCurrentRoom()==-1L) return null;
        final GameRoom userRoom=activeRooms.get(user.getCurrentRoom());
        return userRoom.getOpponent(user);
    }
    public void userExitedRoom(PlayingUser user) {
        if (user.getCurrentRoom()!=-1L) {
            activeRooms.get(user.getCurrentRoom()).userExited(user);
            user.setRoom(-1L);
        }
    }
}
