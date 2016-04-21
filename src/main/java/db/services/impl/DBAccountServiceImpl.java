package db.services.impl;

import db.models.User;
import db.services.AccountService;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;

/**
 * created: 28-Mar-16
 * package: db.services.impl
 */
public class DBAccountServiceImpl implements AccountService{

    @Override
    public Collection<User> getUsers() {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasUser(long id) {
        throw new NotImplementedException();

    }

    @Override
    public boolean hasUser(@NotNull String username) {
        throw new NotImplementedException();

    }

    @Override
    public long addUser(@NotNull User user) {
        throw new NotImplementedException();

    }

    @Override
    public boolean removeUser(long id) {
        throw new NotImplementedException();

    }

    @Override
    public boolean removeUser(@NotNull String username) {
        throw new NotImplementedException();

    }

    @NotNull
    @Override
    public User getUser(long id) {
        throw new NotImplementedException();

    }

    @NotNull
    @Override
    public User getUser(@NotNull String username) {
        throw new NotImplementedException();

    }
}
