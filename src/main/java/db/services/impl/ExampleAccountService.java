package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.Marker;
//import org.apache.logging.log4j.core.appender.SyslogAppender;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class ExampleAccountService implements AccountService {
    private static final Logger logger = LogManager.getLogger(ExampleAccountService.class);

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private ConcurrentMap<Long, User> table_id_users = new ConcurrentHashMap<>();
    private ConcurrentMap<String, WeakReference<User>> table_name_users = new ConcurrentHashMap<>();
    private Map<String, User> users=new HashMap<>();
    private Map<Long, String> userids=new HashMap<>();



    public ExampleAccountService() {
        addUser(new User("admin@admin.ru", "admin"));
        addUser(new User("guest@mail.ru", "12345"));
    }


    public Collection<User> getAllUsers() {
        return users.values();
    }

    public boolean addUser(Long userId, User user) {
        boolean _id_users_changed = false,
                _name_users_changed = false;
        try {
            if(this.table_id_users.containsKey(userId))
                return false;
            else {
                User put_user = this.table_id_users.put(userId, user);
                _id_users_changed = true;
                _name_users_changed = true;
                this.table_name_users.put(user.getLogin(), new WeakReference<>(put_user)); // ?
            }
        }
        catch (Exception e) {
            // Rollback changes
            if (_id_users_changed)
                this.table_id_users.remove(userId);
            if(_name_users_changed)
                this.table_name_users.remove(user.getLogin());
            logger.error("Error");
            return false;
        }
        return true;
    }
    public Long addUser(User user) {
        Long value = new Long(this.users.size());
        user.setId(value+1);
        this.addUser(value, user);
        users.put(user.getLogin(), user);
        userids.put(value, user.getLogin());
        return value;
    }

    public User getUser(Long userId) {
        if (userId == null)
            return null;
        return this.users.get(userids.get(userId));
    }

    public User getUser(String userName) {
        //
        return this.users.get(userName);
    }
}
