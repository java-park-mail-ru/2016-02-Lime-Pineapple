package db.services.impl;

import db.models.User;
import db.services.AccountService;

import java.util.Collection;

/**
 * created: 28-Mar-16
 * package: db.services.impl
 */
public class DBAccountServiceImpl implements AccountService{
    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public boolean addUser(Long userId, User user) {
        return false;
    }

    @Override
    public Long addUser(User user) {
        return null;
    }

    @Override
    public User getUser(Long userId) {
        return null;
    }

    @Override
    public User getUser(String userName) {
        return null;
    }

    @Override
    public Collection<String> getUserScores() {
        return null;
    }
}
