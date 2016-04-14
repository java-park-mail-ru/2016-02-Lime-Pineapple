package db.models.game;

import db.models.User;
import org.jetbrains.annotations.NotNull;

/**
 * created: 12-Mar-16
 * package: db.models.game
 */


public class PlayingUser {
    User linkedUser;
    Long currentScore;

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

    public void incrementScore(Long delta) {
        this.currentScore += delta;
    }


    public PlayingUser(@NotNull  User user) {
        this.linkedUser = user;
        this.currentScore = 0L;
    }
}
