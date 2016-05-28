package db.models;

import org.jetbrains.annotations.NotNull;

/**
 * created: 5/27/2016
 * package: db.models
 */
public class UserScore {
    @NotNull
    String username;
    @NotNull
    Integer score;
    @NotNull
    Integer bestScore;
    @NotNull
    Integer playedGames;

    public UserScore() {

    }

    public UserScore(UserScore score) {
        this.username = score.getUsername();
        this.score = score.getScore();
        this.bestScore = score.getBestScore();
        this.playedGames = score.getPlayedGames();
    }


    public UserScore(@NotNull String username, @NotNull Integer score, @NotNull Integer bestScore, @NotNull Integer playedGames) {
        this.username = username;
        this.score = score;
        this.bestScore = bestScore;
        this.playedGames = playedGames;
    }

    public UserScore(@NotNull User user) {
        this.username = user.getUsername();
        this.bestScore = user.getBestScore();
        this.score = user.getScore();
        this.playedGames = user.getPlayedGames();

    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public Integer getScore() {
        return score;
    }

    public void setScore(@NotNull Integer score) {
        this.score = score;
    }

    @NotNull
    public Integer getBestScore() {
        return bestScore;
    }

    public void setBestScore(@NotNull Integer bestScore) {
        this.bestScore = bestScore;
    }

    @NotNull
    public Integer getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(@NotNull Integer playedGames) {
        this.playedGames = playedGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserScore)) return false;

        final UserScore userScore = (UserScore) o;

        if (!getUsername().equals(userScore.getUsername())) return false;
        if (!getScore().equals(userScore.getScore())) return false;
        if (!getBestScore().equals(userScore.getBestScore())) return false;
        return getPlayedGames().equals(userScore.getPlayedGames());

    }

    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + getScore().hashCode();
        result = 31 * result + getBestScore().hashCode();
        result = 31 * result + getPlayedGames().hashCode();
        return result;
    }
}
