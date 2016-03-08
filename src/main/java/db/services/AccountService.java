package db.services;

import db.models.User;
import java.util.Collection;


public interface AccountService {
    Collection<User> getAllUsers();

    boolean addUser(Long userId, User user);
    boolean addUser(User user);

    User getUser(Long userId);
    User getUser(String userName);
}
