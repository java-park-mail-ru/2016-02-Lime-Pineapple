package game;

import db.models.game.cards.BaseCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class GameRoom {
    static final Logger LOGGER = LogManager.getLogger(GameRoom.class);
    private RoomStatus roomStatus = RoomStatus.LOOKING_FOR_PEOPLE; //класс должен быть абстрактным или иметь переопределенный метод
    //GAME VALUES
    private List<PlayingUser> userHands; //cards which users possess and able to activate

    @NotNull
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public boolean setRoomStatus(@NotNull  RoomStatus room) {
        roomStatus = room;
        //trigger event
        return true;
    }


}
