package db.models.game;

import db.models.User;
import db.models.game.cards.BaseCard;
import db.models.validation.IValidate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Vector;

public class GameRoom implements IValidate {
    private RoomStatus roomStatus;

    private short totalCards;
    private short playedCards;
    private BaseCard[][] Deck;
    private HashMap<PlayingUser, BaseCard> userHands;

    private Vector<PlayingUser> clickers_team, deck_team;


    @NotNull
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public boolean setRoomStatus(@NotNull  RoomStatus room) {
        try {
            roomStatus = room;


            return true;
        }
        catch (Throwable t) {
            System.out.println("Error setting status");
            return false;
        }
    }

    public void Validate() {

    }

    public GameRoom() {
         setRoomStatus(RoomStatus.LOOKING_FOR_PEOPLE);
    }


}
