package game;

import db.models.User;
import org.jetbrains.annotations.NotNull;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */

// This class describes user state during game
public class PlayingUser {
    private User linkedUser;
    private long currentScore = 0;
    private short lives = 0;
    private long connectionId = 0;

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


    public PlayingUser(@NotNull  User user) {
        this.linkedUser = user;
    }

}
