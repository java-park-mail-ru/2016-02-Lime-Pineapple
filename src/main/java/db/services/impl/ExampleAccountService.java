package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Raaw on 02-Mar-16.
 */
public class ExampleAccountService implements AccountService {
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountService.class);

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private ConcurrentMap<Long, User> tableIdUsers = new ConcurrentHashMap<>();
    private ConcurrentMap<String, WeakReference<User>> tableNameUsers = new ConcurrentHashMap<>();



    public ExampleAccountService() {
        addUser(new User("admin@admin.ru", "admin"));
        addUser(new User("guest@mail.ru", "12345"));
    }

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
                return false;
            else {
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
            return false;
        }
        return true;
    }

    @Override
    public Long addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
        user.setId(value);
        return this.addUser(value, user) ? value : 0;
    }

    @Nullable @Override
    public User getUser(@NotNull  Long userId) {
        return this.tableIdUsers.getOrDefault(userId, null);
    }

    @Nullable @Override
    public User getUser(@NotNull String userName) {
        return this.tableNameUsers.get(userName).get();
    }
}
