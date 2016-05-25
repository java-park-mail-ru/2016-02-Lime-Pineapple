package db.models.game;


import db.models.game.cards.BaseCard;
import db.models.validation.IValidate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Vector;

public class GameRoom implements IValidate {
    private RoomStatus roomStatus; //класс должен быть абстрактным или иметь переопределенный метод
    //GAME VALUES
    private short totalCards;
    private short playedCards=0;
    private BaseCard[][] deck; //current game deck
    private PlayingUser creator;
    private PlayingUser opponent;
    private long id;
    private HashMap<PlayingUser, BaseCard> userHands; //cards which users possess and able to activate
    // TODO: change this to store info about playing users in particular room only in one place (for example: in cookies)



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
    public void validate() {
        //does nothing
    }
    public void setId(long id1) {
        this.id=id1;
    }

    public long getId() {
        return id;
    }

    public GameRoom(PlayingUser autor, short cards) {
        creator= autor;
        totalCards=cards;
        setRoomStatus(RoomStatus.WAITING_FOR_PLAYERS);
    }
    public void opponentAdded(PlayingUser user) {
        opponent = user;
        setRoomStatus(RoomStatus.PREPARING_FOR_GAME);
    }
    public void startGame() {
        setRoomStatus(RoomStatus.GAME_PHASE);
    }
    public void pauseGame() {
        setRoomStatus(RoomStatus.PAUSE_PHASE);
    }
    public void continueGame() {
        setRoomStatus(RoomStatus.GAME_PHASE);
    }
    public int playCard() {
        playedCards++;
        //Что происходит при разыгрывании карты
        if (playedCards>=totalCards) setRoomStatus(RoomStatus.FINISHED);
        return 1;
    }
}
