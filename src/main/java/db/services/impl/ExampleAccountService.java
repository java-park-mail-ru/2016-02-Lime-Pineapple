package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class ExampleAccountService extends AccountService {
    private static final Logger logger = LogManager.getLogger(ExampleAccountService.class);

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private ConcurrentMap<Long, User> table_id_users = new ConcurrentHashMap<>();
    private ConcurrentMap<String, User> table_name_users = new ConcurrentHashMap<>();



    public ExampleAccountService() {
        addUser(new User("admin@admin.ru", "admin", "Admin"));
        addUser(new User("guest@mail.ru", "12345", "Guest"));
    }


    public Collection<User> getAllUsers() {
        return table_id_users.values();
    }

    public boolean addUser(Long userId, User user) {
        boolean _id_users_changed = false, _name_users_changed = false;
        try {
            if(this.table_id_users.containsKey(userId))
                return false;
            else {
                this.table_id_users.put(userId, user);
                _id_users_changed = true;
                _name_users_changed = true;
                this.table_name_users.put(user.getLogin(), user);

            }
        }
        catch (Exception e) {
            // Rollback changes
            if (_id_users_changed)
                this.table_id_users.remove(userId);
            if(_name_users_changed)
                this.table_name_users.remove(user.getLogin());
            logger.error("Error during adding user");
            return false;
        }
        return true;
    }
    public boolean addUser(User user) {
        user.setId(this.autoIncrementId.incrementAndGet());
        return this.addUser(user.getId(), user);

    }

    public User getUser(Long userId) {
        if (userId == null)
            return null;
        return this.table_id_users.getOrDefault(userId, null);
    }

    public User getUser(String userName) {
        return this.table_name_users.get(userName);
    }
}
