package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;


public class ExampleAccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountServiceImpl.class);

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private ConcurrentMap<Long, String> tableIdUsers = new ConcurrentHashMap<>();
    private ConcurrentMap<String, User> tableNameUsers = new ConcurrentHashMap<>();
    private static final int USERLIMIT = 100000;
    public ExampleAccountServiceImpl() {
        this.addUser(new User("admin@admin.ru", "admin"));
        this.addUser(new User("guest@mail.ru", "12345"));
        this.getUser("admin@admin.ru").increaseScore(10);
    }

    @Override
    public Collection<User> getUsers() {
        return tableNameUsers.values();
    }
    @Override
    public boolean addUser(@NotNull Long userId, @NotNull User user) {
        if(this.tableIdUsers.containsKey(userId))
            return false;
        else {
            user.setId(userId);
            boolean validated=false;
            if (validateUser(user).equals("OK")) validated=true;
            else if (validateUser(user).equals("Nickname is too short.") || validateUser(user).equals("Such nicknames are forbidden.")) {
                user.setNickname(user.getLogin());
                validated=true;
            }
            if (user.getNickname().isEmpty()) user.setNickname(user.getLogin());
            if (validated) {
                this.tableIdUsers.put(userId, user.getLogin());
                this.tableNameUsers.put(user.getLogin(), user);
                return true;
            }
            else {
                LOGGER.debug("Invalide user data");
                return false;
            }
        }
    }

    @Override
    public boolean addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
        return this.addUser(value, user);
    }

    @Override
    @NotNull
    public User getUser(@NotNull Long userId) {
        return this.tableNameUsers.getOrDefault(tableIdUsers.get(userId), null);
    }

    @NotNull @Override
    public User getUser(@NotNull String userName) {
        return this.tableNameUsers.get(userName);
    }

    @Override
    public boolean hasUser(long id) {
        return (tableIdUsers.containsKey(id));
    }

    @Override
    public boolean hasUser(@NotNull String username) {

        return (tableNameUsers.containsKey(username));
    }

    @Override
    public boolean removeUser(long id) {
        if(this.hasUser(id)) {

            this.tableNameUsers.remove(tableIdUsers.get(id));
            this.tableIdUsers.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeUser(@NotNull String username) {
        if(this.hasUser(username)) {
            final User user  = this.tableNameUsers.remove(username);
            this.tableIdUsers.remove(user.getId());
            return true;
        }
        return false;
    }
    @Override
    public Collection<String>getUserScores() {
        final Map<Integer, String> playerscores=new TreeMap<>();
        final Iterator<Map.Entry<String, User>> allusers=tableNameUsers.entrySet().iterator();
        while (allusers.hasNext()) {
            final User current=allusers.next().getValue();
            final String score=current.getLogin() + " scored " + current.getScore().toString();
            playerscores.put(current.getScore(), score);
        }
        return playerscores.values();
    }
    @Override
    public int getUsersLimit() {
        return USERLIMIT;
    }
    @Override
    public int getUsersCount() {
        return tableNameUsers.size();
    }
    @Override
    public boolean changeUser(@NotNull User user)
    {
        if (tableNameUsers.containsKey(user.getLogin())) {

            final User updatingUser = tableNameUsers.get(user.getLogin());
            final String validationResult=validateUser(user);
            if (!validationResult.equals("Nickname is too short.") && !validationResult.equals("Such nicknames are forbidden.")) {
                updatingUser.setNickname(user.getNickname());
            }
            if (!validationResult.equals("Password is too short.")) {
                updatingUser.setPassword(user.getPassword());
            }
            tableNameUsers.put(user.getLogin(), updatingUser);
            LOGGER.info("User was updated");
            return true;
        }
        else return false;
    }
    public String validateUser(User user) {

        if (user.getLogin().isEmpty()) {
            return "Login invalid";
        }
        else if (user.getPassword().isEmpty() || user.getPassword().length()<5) {

            return "Password is too short.";
        }
        else if (user.getNickname().length()<5) {
            return "Nickname is too short.";
        }
        else if (user.getNickname().matches("fuck") || user.getNickname().matches("shit") || user.getNickname().matches("ass") || user.getNickname().matches("Hitler") || user.getNickname().matches("porn") || user.getNickname().matches("dick")) {
            return "Such nicknames are forbidden.";
        }
        LOGGER.debug("[ + ] UserData is valid");
        return "OK";
    }
}

