package db.models;

import db.models.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import javax.persistence.*;

@Entity
@Table (name="USERS")
public class User  {
    /*private enum UserValidationErrors {
        LOGIN_INVALID,
        NICKNAME_TOO_SHORT,
        NICKNAME_INVALID,
        PASSWORD_WEAK, //use this for raw password
    };
    private final static int
            VALIDATION_MIN_NICKNAME_LENGTH = 4,
            VALIDATION_MIN_PASSWORD_LENGTH = 5;
    private static final int VALIDATION_MIN_PASSWORD_LENGTH = 5;*/
    private final static Logger logger = LogManager.getLogger(User.class);

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
    private Integer totalScore; //total score for all games

    public User() {
        login = "";
        password="";
        totalScore = 0;
        nickname = "";
        logger.debug("[+] Empty instance created.");
    }

    public User(@NotNull String login, @NotNull String password) {

        this.setLogin(login);
        this.setPassword(password);
        totalScore = 0;
        logger.debug("[+] Non-empty instance created.");
    }

    @NotNull
    public String getLogin() {
        return login;
    }
    public void setLogin(@NotNull String login1) {
            this.login=login1;
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

    public void setPassword(@NotNull String password1) {

        this.password=password1;
    }

    @NotNull
    public String getNickname() {
        if (!nickname.isEmpty()) return nickname;
        else return login;
    }
    public void setNickname(@NotNull String nickname){

         this.nickname=nickname;
    }

    @NotNull
    public Integer getScore() { return this.totalScore; }
    public void increaseScore(int delta) { this.totalScore+=delta; }
    public void setScore(int score) { this.totalScore = score; }

    public void Validate() {
        if ( !this.getLogin().matches( "/.+@.+\\..+/i" ) ) //any_symbol@any_symbol.any_symbol
            throw new ValidationException("Name invalid", (long)UserValidationErrors.LOGIN_INVALID.ordinal());
        else if ( this.getPassword().length() < VALIDATION_MIN_PASSWORD_LENGTH )
            throw new ValidationException("Password is too short.", (long)UserValidationErrors.PASSWORD_WEAK.ordinal());
        else if ( this.getNickname().length() < VALIDATION_MIN_NICKNAME_LENGTH )
            throw new ValidationException("Nickname is too short.", (long)UserValidationErrors.NICKNAME_TOO_SHORT.ordinal());
        else if ( this.getNickname().matches("/w+/i") )
        logger.debug("[ + ] UserData is valid");
    }
}
