package db.models;

import db.models.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;

@Entity
@Table (name="USERS")
public class User  {
    private enum UserValidationErrors {
        LOGIN_INVALID,
        NICKNAME_TOO_SHORT,
        NICKNAME_INVALID,
        PASSWORD_WEAK, //use this for raw password
        EMPTY_PASSWORD,
        EMPTY_LOGIN,
        FORBIDDEN_NICKNAME
    }
    private static final int
            VALIDATION_MIN_NICKNAME_LENGTH = 4;
    private static final int VALIDATION_MIN_PASSWORD_LENGTH = 5;
    private static final Logger LOGGER = LogManager.getLogger(User.class);

    @NotNull
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id           = 0L;
    @NotNull
    @Column(name="LOGIN")
    private String  login        = "";
    @NotNull
    @Column(name="PASSWORD")
    private String  password     = "";
    @NotNull
    @Column(name="NICKNAME")
    private String nickname="";
    @Column(name="TOTALSCORE")
    private Integer totalScore;

    public User() {
        login = "";
        setPassword("");
        totalScore = 0;
        nickname = "";
        LOGGER.debug("[+] Empty instance created.");
    }

    public User(@NotNull String login, @NotNull String password) throws ValidationException {

        setLogin(login);
        setPassword(password);
        totalScore = 0;
        LOGGER.debug("[+] Non-empty instance created.");
    }

    @NotNull
    public String getLogin() {
        return login;
    }
    public String setLogin(@NotNull String login1) throws ValidationException{
        try {validatevalue("Login", login1);
            this.login=login1;
        }
        catch (ValidationException e) {
            if (this.login.isEmpty()) LOGGER.error(e.getMessage());
            else LOGGER.error("Login not changed" +e.getMessage());
            throw e;
        }
        return this.login;
    }

    @NotNull
    public Long getId() {
        return this.id;
    }

    public Long setId(@NotNull Long id1) {
        return this.id = id1;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public String setPassword(@NotNull String password1) throws ValidationException {

        try {
            validatevalue("Password", password1);
            this.password=password1;
        }
        catch (ValidationException e) {
            if (this.password.isEmpty()) LOGGER.error(e.getMessage());
            else LOGGER.error("Password not changed" +e.getMessage());
            throw e;
        }
        return this.password;
    }

    @NotNull
    public String getNickname() { return nickname; }
    public void setNickname(@NotNull String nickname) throws ValidationException{
        try {validatevalue("Nickname", nickname);
            this.nickname=nickname;
        }
        catch (ValidationException e) {
            if (this.nickname.isEmpty()) LOGGER.error("Nickname not set"+e.getMessage());
            else LOGGER.error("Nickname not changed" +e.getMessage());
            throw e;
        }
    }

    @NotNull
    public Integer getScore() { return this.totalScore; }
    public void increaseScore(int delta) { this.totalScore+=delta; }

    private void validatevalue(String field, String value) throws ValidationException{
        if (field.equals("Login")) {
            if (value.isEmpty())
                throw new ValidationException("Name invalid", (long) UserValidationErrors.LOGIN_INVALID.ordinal());
        }
        else if (field.equals("Password") && !value.isEmpty()) {
            if (value.length()<VALIDATION_MIN_PASSWORD_LENGTH)
                throw new ValidationException("Password is too short.", (long)UserValidationErrors.PASSWORD_WEAK.ordinal());
        }
        else if (field.equals("Password") && value.isEmpty()) {
            throw new ValidationException("Password is Empty.", (long)UserValidationErrors.EMPTY_PASSWORD.ordinal());
        }
        else if (field.equals("Nickname")) {
            if (value.length() < VALIDATION_MIN_NICKNAME_LENGTH && !value.isEmpty())
                throw new ValidationException("Nickname is too short.", (long)UserValidationErrors.NICKNAME_TOO_SHORT.ordinal());
            else if (value.matches("fuck") || value.matches("shit") || value.matches("ass") || value.matches("Hitler") || value.matches("porn") || value.matches("dick")) {
                throw new ValidationException("Such nicknames are forbidden.", (long)UserValidationErrors.FORBIDDEN_NICKNAME.ordinal());
            }

        }

        LOGGER.debug("[ + ] UserData is valid");
    }

}
