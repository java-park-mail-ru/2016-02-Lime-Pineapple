package db.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class User {
    private final static Logger logger = LogManager.getLogger(User.class);
    private Long    id           = 0L;
    private String  login        = "";
    private String  password     = "";

    public User() {
        logger.debug("[+] Empty instance created.");
    }

    public User(@NotNull String login, @NotNull String password) {
        this.login = login;
        this.password = password;
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
}
