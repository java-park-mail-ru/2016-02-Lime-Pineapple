package db.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table (name="User")
public class User  {
    private static final Logger LOGGER = LogManager.getLogger(User.class);

    @NotNull
    @Id
    @Column(name="Id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long    id           = 0L;
    @NotNull
    @Column(name="Username", unique = true)
    private String username = "";
    @NotNull
    @Column(name="Password")
    private String  password     = "";
    @NotNull
    @Column(name="Nickname")
    private String nickname = "";
    @NotNull
    @Column(name="Score")
    private Integer score = 0; //total score for all games
    @NotNull
    @Column(name="PlayedGames")
    private Integer playedGames = 0;
    @NotNull
    @Column(name="BestScore")
    private Integer bestScore = 0;

    public User() {

    }


    public User(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        this.nickname = user.getNickname();

        this.score = user.getScore();
        this.bestScore = user.getBestScore();
        this.playedGames = user.getPlayedGames();
        this.id = user.getId();
    }
    public User(@NotNull String username, @NotNull String password) {

        LOGGER.debug("[+] Non-empty User instance created.");
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    @NotNull
    public String getUsername() {
        return this.username;
    }
    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public Long getId() {
        return this.id;
    }
    public Long setId(@NotNull Long id) {
        return this.id = id;
    }

    @NotNull
    public String getPassword() {
        return password;
    }
    public void setPassword(@NotNull String password) {

        this.password=password;
    }

    @NotNull
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(@NotNull String nickname){
        this.nickname=nickname;
    }

    @NotNull
    public Integer getScore() { return this.score; }
    public void increaseScore(int delta) { this.score +=delta; }
    public void setScore(int score) { this.score = score; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        final User user = (User) o;

        if (!getId().equals(user.getId())) return false;
        if (!getUsername().equals(user.getUsername())) return false;
        if (!getPassword().equals(user.getPassword())) return false;
        if (!getNickname().equals(user.getNickname())) return false;
        if (!getScore().equals(user.getScore())) return false;
        if (!getPlayedGames().equals(user.getPlayedGames())) return false;
        return getBestScore().equals(user.getBestScore());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getNickname().hashCode();
        result = 31 * result + getScore().hashCode();
        result = 31 * result + getPlayedGames().hashCode();
        result = 31 * result + getBestScore().hashCode();
        return result;
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
}