package db.models.game;

import java.awt.event.ActionListener;

public abstract class GameRoom implements ActionListener {
    private RoomStatus roomStatus; //класс должен быть абстрактным или иметь переопределенный метод actionlistener
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }
    public boolean setRoomStatus(RoomStatus room) {
        try {
            roomStatus = room;
            return true;
        }
        catch (Throwable t) {
            System.out.println("Error setting status");
            return false;
        }

    }
    public GameRoom() {
         setRoomStatus(RoomStatus.LOOKING_FOR_PEOPLE);
    }
}
