package db.services.impl;

import db.models.game.ScoreTable;
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
    private static final Logger logger = LogManager.getLogger(ExampleAccountService.class);
    private Map<String, User> users = new HashMap<>();
    private Map<Long, String> userids = new HashMap<>();



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
                logger.error("User with this id already exists");
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

            logger.error("Error");
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
            logger.error("User was not registrated");
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

    public Collection<ScoreTable>getuserScores() {
        Map<Integer, ScoreTable> playerscores=new TreeMap<>();
        Iterator<Map.Entry<String, User>>allusers=users.entrySet().iterator();
        User current;
        while (allusers.hasNext()) {
            current=allusers.next().getValue();
            final ScoreTable score=new ScoreTable(current.getScore(),current.getNickname());
            playerscores.put(current.getScore(), score);
        }
        return playerscores.values();
    }

}
