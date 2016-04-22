package db.models.game;


import db.models.User;

public interface GameRoomEventHandler {
    void OnStatusChanged(GameRoom gameRoom, RoomStatus newStatus);
    void OnUserJoined( GameRoom gameRoom  , User user);
    void OnUserLeft( GameRoom gameRoom  , User user);
    void OnParameterChanged( GameRoom gameRoom, String paramName);
    void OnGameCreated(GameRoom gameRoom); //happens when game is initialized and put to db
}
