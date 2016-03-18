package db.models.game;

import org.jetbrains.annotations.NotNull;


public class ScoreTable {
    @NotNull
    private Integer Score;
    @NotNull
    private String Nickname;
    public ScoreTable(@NotNull Integer score, @NotNull String name) {

        this.Score = score;
        this.Nickname = name;
    }
    @NotNull Integer getScore() {
        return Score;
    }
    @NotNull  String getNickname() {
        return Nickname;
    }

}
