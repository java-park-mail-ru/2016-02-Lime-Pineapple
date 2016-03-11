package db.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class User {
    private final static Logger logger = LogManager.getLogger(User.class);
    @NotNull
    private Long    id           = 0L;
    @NotNull
    private String  login        = "";
    @NotNull
    private String  password     = "";
    @NotNull
    private String nickname="";
    private Integer score;

    public User() {
        login = "";
        password = "";
        score = 0;
        nickname = "";
        logger.debug("[+] Empty instance created.");
    }

    public User(@NotNull String login, @NotNull String password) {

        setLogin(login);
        setPassword(password);
        score = 0;
        logger.debug("[+] Non-empty instance created.");
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public String setLogin(@NotNull String login) {
        return this.login = login;
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

    public String setPassword(@NotNull String password) {
        return this.password = password;
    }

    @NotNull
    public String getNickname() { return nickname; }
    public void setNickname(@NotNull String nickname) { this.nickname = nickname; }

    @NotNull
    public Integer getScore() { return this.score; }
    public void increaseScore(int delta) { this.score+=delta; }
    public void setScore(int score) { this.score = score; }
}
