package db.models.game;

import db.models.User;
import db.models.game.cards.BaseCard;
import db.models.validation.IValidate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Vector;

public class GameRoom implements IValidate {
    private RoomStatus roomStatus; //класс должен быть абстрактным или иметь переопределенный метод
    //GAME VALUES
    private short totalCards;
    private short playedCards;
    private BaseCard[][] Deck; //current game deck
    private HashMap<PlayingUser, BaseCard> userHands; //cards which users possess and able to activate
    // TODO: change this to store info about playing users in particular room only in one place (for example: in cookies)
    private Vector<PlayingUser> clickers_team, deck_team;


    @NotNull
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public boolean setRoomStatus(@NotNull  RoomStatus room) {
        try {
            roomStatus = room;
            //trigger event

            return true;
        }
        catch (Throwable t) {
            System.out.println("Error setting status");
            return false;
        }
    }

    @Override
    public void Validate() {
        //does nothing
    }

    public GameRoom() {
         setRoomStatus(RoomStatus.LOOKING_FOR_PEOPLE);
    }



}
