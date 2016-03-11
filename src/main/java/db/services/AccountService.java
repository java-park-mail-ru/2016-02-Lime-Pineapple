package db.services;

import db.models.User;
import java.util.Collection;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
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
        if (ValidateData(user)) {
            users.put(userName, user);
            return true;
        }
        else {
            logger.error("Error, user with this name already exists");
            return false;
        }
    }

    public boolean addUser(User user){
        if (ValidateData(user)) {
            users.put(user.getLogin(), user);
            return true;
        }
        else {
            logger.error("Error, user with this name already exists");
            return false;
        }
    }
    public User getUser(Long userId) {
        Iterator userids = users.entrySet().iterator();
        boolean found = false;
        User requireUser=null;
        while (userids.hasNext() && !found) {
            Map.Entry variant = (Map.Entry)userids.next();
            try {
                String key=variant.getKey().toString();
                requireUser = users.get(key);
                if (requireUser.getId().equals(userId)) {
                    found=true;
                }
            }
            catch (Throwable t) {
                System.err.println("Error getting user");
            }
        }
        return requireUser;
    }
    public User getUser(String userName){
        return users.get(userName);
    }
    public boolean ValidateData(User regUser) {
        if (regUser.getLogin().equals("") || regUser.getPassword().equals("") || regUser.getNickname().equals("")) return false;
        else if (users.containsKey(regUser.getLogin())){
            System.err.println("User with this login already exists");
            return false;
        }
        else {
            System.out.print("UserData is valid");
            return true;
        }
    }

}
