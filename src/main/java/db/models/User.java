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
    private String nickname="";
    @Column(name="score")
    private Integer score; //total score for all games

    public User() {
        username = "";
        password="";
        score = 0;
        nickname = "";
        LOGGER.debug("[+] Empty instance created.");
    }
    public User(@NotNull String username, @NotNull String password) {

        this.username = username;
        this.password = password;
        this.score = 0;
        LOGGER.debug("[+] Non-empty instance created.");
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
        if (!nickname.isEmpty()) return nickname;
        else return username;
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

        User user = (User) o;

        if (!getId().equals(user.getId())) return false;
        if (!getUsername().equals(user.getUsername())) return false;
        if (!getPassword().equals(user.getPassword())) return false;
        if (!getNickname().equals(user.getNickname())) return false;
        return getScore().equals(user.getScore());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + getNickname().hashCode();
        result = 31 * result + getScore().hashCode();
        return result;
    }
}
