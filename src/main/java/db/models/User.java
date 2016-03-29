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
        PASSWORD_WEAK, //use this for raw password
    }


    private static final int
            VALIDATION_MIN_NICKNAME_LENGTH = 4;
    private static final int VALIDATION_MIN_PASSWORD_LENGTH = 5;



    private static final Logger LOGGER = LogManager.getLogger(User.class);
    @NotNull
    private Long    id           = 0L;
    @NotNull
    private String  login        = "";
    @NotNull
    private Integer  password     = 0;
    @NotNull
    private String nickname="";

    private Integer totalScore;

    public User() {
        login = "";
        setPassword("");
        totalScore = 0;
        nickname = "";
        LOGGER.debug("[+] Empty instance created.");
    }

    public User(@NotNull String login, @NotNull String password) {

        setLogin(login);
        setPassword(password);
        totalScore = 0;
        LOGGER.debug("[+] Non-empty instance created.");
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public String setLogin(@NotNull String login1) {
        return this.login = login1;
    }

    @NotNull
    public Long getId() {
        return this.id;
    }

    public Long setId(@NotNull Long id1) {
        return this.id = id1;
    }

    @NotNull
    public Integer getPassword() {
        return password;
    }

    public Integer setPassword(@NotNull String password1) {
        return this.password = password1.hashCode();
    }

    @NotNull
    public String getNickname() { return nickname; }
    public void setNickname(@NotNull String nickname) { this.nickname = nickname; }

    @NotNull
    public Integer getScore() { return this.totalScore; }
    public void increaseScore(int delta) { this.totalScore+=delta; }
    public void setScore(int score) { this.totalScore = score; }

    public void validate() {
        if ( !this.login.matches( "/.+@.+\\..+/i" ) )
            throw new ValidationException("Name invalid", (long)UserValidationErrors.LOGIN_INVALID.ordinal());
        else if ( this.getPassword().toString().length() < VALIDATION_MIN_PASSWORD_LENGTH )
            throw new ValidationException("Password is too short.", (long)UserValidationErrors.PASSWORD_WEAK.ordinal());
        else if ( this.nickname.length() < VALIDATION_MIN_NICKNAME_LENGTH )
            throw new ValidationException("Nickname is too short.", (long)UserValidationErrors.NICKNAME_TOO_SHORT.ordinal());
        else if ( this.nickname.matches("/w+/i") )
        LOGGER.debug("[ + ] UserData is valid");
    }
}
