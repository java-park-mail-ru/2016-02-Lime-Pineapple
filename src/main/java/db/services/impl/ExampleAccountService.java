package db.services.impl;

import db.services.AccountService;
import db.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

public class ExampleAccountService implements AccountService {
    private static final Logger logger = LogManager.getLogger(ExampleAccountService.class);
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

        try {
            if(this.userids.containsKey(userId)) {
                logger.error("User with this id already exists");
                return false;
            }
            else {
                users.put(user.getLogin(), user);
                return true;
            }
        }
        catch (Exception e) {

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
