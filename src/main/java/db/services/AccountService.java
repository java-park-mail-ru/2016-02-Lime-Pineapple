package db.services;


import db.models.User;
import java.util.Collection;

public interface AccountService {
    Collection<User> getAllUsers();
    // method allows to define ID manually
    boolean addUser(Long userId, User user);
    // method id automatically and returns it after creation. 0 for fail
    Long addUser(User user);
    User getUser(Long userId);
    User getUser(String userName);
}
