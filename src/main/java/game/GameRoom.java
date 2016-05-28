package game;

import db.models.game.cards.BaseCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class GameRoom {
    static final Logger LOGGER = LogManager.getLogger(GameRoom.class);
    private RoomStatus roomStatus = RoomStatus.LOOKING_FOR_PEOPLE; //класс должен быть абстрактным или иметь переопределенный метод
    //GAME VALUES
    private short totalCards;
    @Nullable
    private String winner;
    private short playedCards=0;
    private BaseCard[][] deck; //current game deck
    @Nullable
    private PlayingUser creator;
    @Nullable
    private PlayingUser opponent;
    private List<BaseCard> creatorHands;
    private List<BaseCard> opponentHands;//cards which users possess and able to activate
    private long id;
    private HashMap<PlayingUser, BaseCard> userHands; //cards which users possess and able to activate
    // TODO: change this to store info about playing users in particular room only in one place (for example: in cookies)
    @NotNull
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public boolean setRoomStatus(@NotNull RoomStatus room) {
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
        opponent=null;
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
        if (playedCards >= totalCards) setRoomStatus(RoomStatus.FINISHED);
        return 1;
    }
    public void userExited(PlayingUser user) {
        if (roomStatus!=RoomStatus.FINISHED) {
            if (user.equals(creator)) {

                opponent.win();
                creator.lose();
                roomStatus = RoomStatus.FINISHED;
                creator = null;
            } else {
                opponent.win();
                creator.lose();
                roomStatus = RoomStatus.FINISHED;
                creator = null;
            }
        }
        else {
            if (user.equals(creator)) {
                creator=null;
            }
            else opponent=null;
        }
    }
    public void gameOver() {
        try {
            if (opponent.getCurrentScore() > creator.getCurrentScore()) {
                opponent.win();
                creator.lose();
            } else {
                creator.win();
                opponent.lose();
            }
            roomStatus=RoomStatus.FINISHED;
        }
        catch (NullPointerException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
