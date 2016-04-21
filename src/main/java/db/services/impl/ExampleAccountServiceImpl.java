package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Raaw on 02-Mar-16.
 */
public class ExampleAccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountServiceImpl.class);

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private ConcurrentMap<Long, User> tableIdUsers = new ConcurrentHashMap<>();
    private ConcurrentMap<String, User> tableNameUsers = new ConcurrentHashMap<>();



    public ExampleAccountServiceImpl() {
        this.addUser(new User("admin@admin.ru", "admin"));
        this.addUser(new User("guest@mail.ru", "12345"));
        this.getUser("admin@admin.ru").increaseScore(10);
    }

    @Override
    public Collection<User> getUsers() {
        return tableIdUsers.values();
    }

    protected long addUser(@NotNull User user, long userId) {
        try {
            if(this.tableIdUsers.containsKey(userId))
                return 0;
            else {
                user.setId(userId);
                this.tableIdUsers.put(userId, user);
                this.tableNameUsers.put(user.getLogin(),user);
            }

        }
        catch (RuntimeException e) {
            this.tableIdUsers.remove(userId);
            this.tableNameUsers.remove(user.getLogin());
            return 0;
        }
        return user.getId();
    }

    @Override
    public long addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
        return this.addUser(user, value) != 0? value : 0;
    }

    @Override
    @NotNull
    public User getUser(long userId) {
        return this.tableIdUsers.getOrDefault(userId, null);
    }

    @NotNull @Override
    public User getUser(@NotNull String userName) {
        return this.tableNameUsers.get(userName);
    }

    @Override
    public boolean hasUser(long id) {
        return false;
    }

    @Override
    public boolean hasUser(@NotNull String username) {
        return false;
    }

    @Override
    public boolean removeUser(long id) {
        if(this.hasUser(id)) {
            final User user  = this.tableIdUsers.remove(id);
            this.tableNameUsers.remove(user.getLogin());
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
}

