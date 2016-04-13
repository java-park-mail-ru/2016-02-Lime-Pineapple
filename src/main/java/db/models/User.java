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

        final String loginStatus=setLogin(login);
        if (!loginStatus.equals("OK")) {
            throw new ValidationException(loginStatus, (long)UserValidationErrors.LOGIN_INVALID.ordinal());
        }
        final String passwordStatus=setPassword(password);
        if (!passwordStatus.equals("OK")) {
            throw new ValidationException(passwordStatus, (long)UserValidationErrors.PASSWORD_WEAK.ordinal());
        }
        totalScore = 0;
        LOGGER.debug("[+] Non-empty instance created.");
    }

    @NotNull
    public String getLogin() {
        return login;
    }
    public String setLogin(@NotNull String login1) {
        final String validationResult=validateValue("Login", login1);
        if (validationResult.equals("OK")) {
            this.login=login1;
        }
        else {
            if (this.login.isEmpty()) LOGGER.error(validationResult);
            else LOGGER.error("Login not changed" +validationResult);
        }
        return validationResult;
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

    public String setPassword(@NotNull String password1) {

        final String validationResult=validateValue("Password", password1);
        if (validationResult.equals("OK")) {
            this.password=password1;
        }
        else {
            if (this.password.isEmpty()) LOGGER.error(validationResult);
            else LOGGER.error("Password not changed" +validationResult);
        }
        return validationResult;
    }

    @NotNull
    public String getNickname() {
        if (!nickname.isEmpty()) return nickname;
        else return login;
    }
    public void setNickname(@NotNull String nickname) throws ValidationException{

        final String validationResult=validateValue("Nickname", nickname);
        if (validationResult.equals("OK")) {
            this.nickname=nickname;
        }
        else {
            if (this.nickname.isEmpty()) LOGGER.error("Nickname not set"+validationResult);
            else LOGGER.error("Nickname not changed" +validationResult);
            throw new ValidationException("Error changing nickname", (long)UserValidationErrors.NICKNAME_INVALID.ordinal());
        }
    }

    @NotNull
    public Integer getScore() { return this.totalScore; }
    public void increaseScore(int delta) { this.totalScore+=delta; }

    private String validateValue(String field, String value) {
        if (field.equals("Login")) {
            if (value.isEmpty())
                return "Login invalid";
        }
        else if (field.equals("Password") && !value.isEmpty()) {
            if (value.length()<VALIDATION_MIN_PASSWORD_LENGTH)
                return "Password is too short.";
        }
        else if (field.equals("Password") && value.isEmpty()) {
            return "Password is Empty.";
        }
        else if (field.equals("Nickname")) {
            if (value.length() < VALIDATION_MIN_NICKNAME_LENGTH && !value.isEmpty())
                return "Nickname is too short.";
            else if (value.matches("fuck") || value.matches("shit") || value.matches("ass") || value.matches("Hitler") || value.matches("porn") || value.matches("dick")) {
                return "Such nicknames are forbidden.";
            }
        }
        LOGGER.debug("[ + ] UserData is valid");
        return "OK";
    }

}
