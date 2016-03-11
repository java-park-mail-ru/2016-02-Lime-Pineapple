package db.models.game;


public abstract class GameRoomEventHandler {

    public abstract void OnStatusChanged(GameRoom gameRoom, RoomStatus newStatus);

}
