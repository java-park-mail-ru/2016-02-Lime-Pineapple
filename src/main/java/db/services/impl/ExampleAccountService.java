package db.services.impl;

import db.models.game.ScoreTable;
import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
<<<<<<< HEAD
import java.util.*;
=======
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
import java.util.concurrent.atomic.AtomicLong;

public class ExampleAccountService implements AccountService {
<<<<<<< HEAD
    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private static final Logger logger = LogManager.getLogger(ExampleAccountService.class);
    private Map<String, User> users = new HashMap<>();
    private Map<Long, String> userids = new HashMap<>();
=======
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountService.class);

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private ConcurrentMap<Long, User> tableIdUsers = new ConcurrentHashMap<>();
    private ConcurrentMap<String, WeakReference<User>> tableNameUsers = new ConcurrentHashMap<>();



>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
    public ExampleAccountService() {
        addUser(new User("admin@admin.ru", "admin"));
        addUser(new User("guest@mail.ru", "12345"));
        users.get("admin@admin.ru").increaseScore(10);
    }

<<<<<<< HEAD
    public Collection<User> getAllUsers() {
        return users.values();
    }
    public boolean addUser(Long userId, User user) {

        try {
            if(this.userids.containsKey(userId)) {
                logger.error("User with this id already exists");
=======
    @Override
    public Collection<User> getAllUsers() {
        return tableIdUsers.values();
    }

    @Override
    public boolean addUser(@NotNull Long userId,@NotNull User user) {
        boolean idUsersTableChanged = false;
        boolean nameUsersTableChanged = false;
        try {
            if(this.tableIdUsers.containsKey(userId))
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
                return false;
            }
            else {
<<<<<<< HEAD
                users.put(user.getLogin(), user);
                return true;
            }
        }
        catch (Exception e) {

            logger.error("Error");
=======
                final User putUser = this.tableIdUsers.put(userId, user);
                idUsersTableChanged = true;
                this.tableNameUsers.put(user.getLogin(), new WeakReference<>(putUser));
                nameUsersTableChanged = true;
            }
        }
        catch (RuntimeException e) {

            if (idUsersTableChanged)
                this.tableIdUsers.remove(userId);
            if(nameUsersTableChanged)
                this.tableNameUsers.remove(user.getLogin());
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
            return false;
        }

    }
<<<<<<< HEAD
    public Long addUser(User user) {
        long value = this.autoIncrementId.incrementAndGet();
=======

    @Override
    public Long addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
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

<<<<<<< HEAD
    public User getUser(Long userId) {
        if (userId == null)
            return null;
        return this.users.get(userids.get(userId));
    }

    public User getUser(String userName) {
        //
        return this.users.get(userName);
=======
    @Nullable @Override
    public User getUser(@NotNull  Long userId) {
        return this.tableIdUsers.getOrDefault(userId, null);
    }

    @Nullable @Override
    public User getUser(@NotNull String userName) {
        return this.tableNameUsers.get(userName).get();
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
    }
    public Collection<ScoreTable>getuserScores() {
        Map<Integer, ScoreTable> player_scores=new TreeMap<>();
        Iterator<Map.Entry<String, User>>all_users=users.entrySet().iterator();
        all_users.forEachRemaining(cur-> {
            User current=cur.getValue();
            ScoreTable score=new ScoreTable(current.getScore(),current.getNickname()+" scored: "+current.getScore()+" points");
            player_scores.put(current.getScore(), score);
        });
        return player_scores.values();
    }

}
