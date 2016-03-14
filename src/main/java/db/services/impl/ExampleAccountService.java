package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.Marker;
//import org.apache.logging.log4j.core.appender.SyslogAppender;

//import java.lang.ref.WeakReference;
import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicLong;


public class ExampleAccountService implements AccountService {
    private static final Logger logger = LogManager.getLogger(ExampleAccountService.class);

    //private AtomicLong autoIncrementId = new AtomicLong(0L);
    //private ConcurrentMap<Long, User> table_id_users = new ConcurrentHashMap<>();
    //private ConcurrentMap<String, WeakReference<User>> table_name_users = new ConcurrentHashMap<>();
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
       // boolean _id_users_changed = false,
       //         _name_users_changed = false;
        try {
            if(this.userids.containsKey(userId)) {
                logger.error("User with this id already exists");
                return false;
            }
            else {
                users.put(user.getLogin(), user);
                return true;
                //_id_users_changed = true;
                //_name_users_changed = true;
                //this.table_name_users.put(user.getLogin(), new WeakReference<>(put_user)); // ?
            }
        }
        catch (Exception e) {
            // Rollback changes
            //if (_id_users_changed)
            //    this.table_id_users.remove(userId);
            //if(_name_users_changed)
            //    this.table_name_users.remove(user.getLogin());
            logger.error("Error");
            return false;
        }

    }
    public Long addUser(User user) {
        long value = (this.users.size());
        user.setId(value+1);
        if (this.addUser(value, user)) {
            userids.put(value, user.getLogin());
            return user.getId();
        }
        else {
            logger.error("User was not registrated");
            return value;
        }

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
    public Collection<String>getuserScores() {
        Map<Integer, String> player_scores=new TreeMap<>();
        Iterator<Map.Entry<String, User>>all_users=users.entrySet().iterator();
        while (all_users.hasNext()) {
            User current=all_users.next().getValue();
            player_scores.put(current.getScore(), current.getNickname()+" scored "+current.getScore().toString()+" points");
        }
        return player_scores.values();
    }

}
