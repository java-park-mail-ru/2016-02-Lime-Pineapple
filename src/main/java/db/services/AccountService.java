package db.services;

import db.models.User;

import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Map;


public class AccountService {
    private final static Logger logger = LogManager.getLogger(AccountService.class);
    public Collection<User> getAllUsers(){
        return users.values();
    }
    Map<String, User> users = new HashMap<>();
    public AccountService(){

    }

    public boolean addUser(String userName, User user){
        if (getUser(userName) == null) {
            users.put(userName, user);
            return true;
        }
        else {
            logger.error("Error, user with this name already exists");
            return false;
        }
    };
    public boolean addUser(User user){
        if (getUser(user.getLogin())==null) {
            users.put(user.getLogin(), user);
            return true;
        }
        else {
            logger.error("Error, user with this name already exists");
            return false;
        }
    }
    public User getUser(Long userId) {
        return null;
    }
    public User getUser(String userName){
        return users.get(userName);
    }
}
