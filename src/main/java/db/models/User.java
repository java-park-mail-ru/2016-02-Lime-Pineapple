package db.models;

import db.models.validation.IValidate;
import db.models.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class User implements IValidate {
    private enum UserValidationErrors {
        LOGIN_INVALID,
        NICKNAME_TOO_SHORT,
        NICKNAME_INVALID,
        PASSWORD_WEAK,
    };
    private final static int
            VALIDATION_MIN_NICKNAME_LENGTH = 4,
            VALIDATION_MIN_PASSWORD_LENGTH = 5;


    private final static Logger logger = LogManager.getLogger(User.class);
    @NotNull
    private Long    id           = 0L;
    @NotNull
    private String  login        = "";
    @NotNull
    private String  password     = "";
    @NotNull
    private String nickname="";

    private Integer totalScore;

    public User() {
        login = "";
        password = "";
        totalScore = 0;
        nickname = "";
        logger.debug("[+] Empty instance created.");
    }

    public User(@NotNull String login, @NotNull String password) {

        setLogin(login);
        setPassword(password);
        totalScore = 0;
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
    public Integer getScore() { return this.totalScore; }
    public void increaseScore(int delta) { this.totalScore+=delta; }
    public void setScore(int score) { this.totalScore = score; }

    public void Validate() {
        if ( !this.getLogin().matches( "/.+@.+\\..+/i" ) )
            throw new ValidationException("Name invalid", (long)UserValidationErrors.LOGIN_INVALID.ordinal());
        else if ( this.getPassword().length() < VALIDATION_MIN_PASSWORD_LENGTH )
            throw new ValidationException("Password is too short.", (long)UserValidationErrors.PASSWORD_WEAK.ordinal());
        else if ( this.getNickname().length() < VALIDATION_MIN_NICKNAME_LENGTH )
            throw new ValidationException("Nickname is too short.", (long)UserValidationErrors.NICKNAME_TOO_SHORT.ordinal());
        else if ( this.getNickname().matches("/w+/i") )
        logger.debug("[ + ] UserData is valid");
    }
}
