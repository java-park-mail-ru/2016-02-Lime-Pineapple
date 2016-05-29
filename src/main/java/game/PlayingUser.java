package game;

import db.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import server.rest.UserServlet;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */

// This class describes user state during game
public class PlayingUser {
    private User linkedUser;
    private long currentScore = 0;
    private short lives = 0;
    private GameRoom room;

    @NotNull
    public User getLinkedUser() {
        return linkedUser;
    }
    @NotNull
    public Long getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(@NotNull Long score) {
        this.currentScore = score;
    }

    public void incrementScore(@NotNull Long delta) {
        this.currentScore += delta;
    }

    @Nullable
    public GameRoom getCurrentRoom() {
        return this.room;
    }

    public PlayingUser(@NotNull  User user) {
        this.linkedUser = user;
    }

    public void win() {
        linkedUser.increaseScore((int) currentScore);
        linkedUser.setPlayedGames(linkedUser.getPlayedGames()+1);
    }
    public void lose() {
        linkedUser.increaseScore((int) currentScore/2);
        linkedUser.setPlayedGames(linkedUser.getPlayedGames()+1);
    }
}
