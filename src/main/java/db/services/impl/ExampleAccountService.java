package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//import java.lang.ref.WeakReference;
import java.util.Collection;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class ExampleAccountService implements AccountService {

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountService.class);
    private Map<String, User> users = new HashMap<>();
    private Map<Long, String> userids = new HashMap<>();
    private final int userlimit = 100000;



//    private ConcurrentMap<Long, User> tableIdUsers = new ConcurrentHashMap<>();
//    private ConcurrentMap<String, WeakReference<User>> tableNameUsers = new ConcurrentHashMap<>();


    public ExampleAccountService() {
        addUser(new User("admin@admin.ru", "admin"));
        addUser(new User("guest@mail.ru", "12345"));
        users.get("admin@admin.ru").increaseScore(10);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public boolean addUser(@NotNull Long userId, @NotNull User user) {

        try {
            if (this.userids.containsKey(userId)) {
                LOGGER.error("User with this id already exists");
                return false;
            }

//    public boolean addUser(@NotNull Long userId,@NotNull User user) {
                //       boolean idUsersTableChanged = false;
                //       boolean nameUsersTableChanged = false;
            else {
                users.put(user.getLogin(), user);
                return true;
            }

        } catch (RuntimeException e) {

            LOGGER.error("Error");
            return false;
        }
        
    }
    @Override
    public Long addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
        user.setId(value);
        if (this.addUser(value, user)) {
            userids.put(value, user.getLogin());
            return user.getId();
        }
        else {
            LOGGER.error("User was not registrated");
            return value;
        }

    }
    @Nullable @Override

    public User getUser(@NotNull Long userId) {

        return this.users.get(userids.get(userId));
    }
    @Nullable @Override
    public User getUser(@NotNull String userName) {
        //
        return this.users.get(userName);
    }
    @Override

    public Collection<String>getuserScores() {
        final Map<Integer, String> playerscores=new TreeMap<>();
        final Iterator<Map.Entry<String, User>>allusers=users.entrySet().iterator();
        while (allusers.hasNext()) {
            final User current=allusers.next().getValue();
            final String score=current.getLogin() + " scored " + current.getScore().toString();
            playerscores.put(current.getScore(), score);
        }
        return playerscores.values();
    }
    @Override
    public boolean changeUser(@NotNull User user)
    {
        if (users.containsKey(user.getLogin())) {
            users.put(user.getLogin(), user);
            return true;
        }
        else return false;
    }
    @Override
    public boolean removeUser(@NotNull Long userid) {
        if (userids.containsKey(userid)) {
            final String username = userids.get(userid);
            users.remove(username);
            userids.remove(userid);
            LOGGER.error("User "+username + " was deleted");
            return true;
        }
        else {
            LOGGER.error("User with id "+userid +" not found");
            return false;
        }
    }
    @Override
    public int getUsersLimit() {
        return userlimit;
    }
    @Override
    public int getUsersCount() {
        return users.size();
    }



}
