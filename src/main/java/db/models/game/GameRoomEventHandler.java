package db.models.game;


import db.models.User;

public interface GameRoomEventHandler {
    void onStatusChanged(GameRoom gameRoom, RoomStatus newStatus);
    void onUserJoined( GameRoom gameRoom  , User user);
    void onUserLeft( GameRoom gameRoom  , User user);
    void onParameterChanged( GameRoom gameRoom, String paramName);
    void onGameCreated(GameRoom gameRoom);
}
