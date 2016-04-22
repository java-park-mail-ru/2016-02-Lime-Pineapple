package db.services.impl;

import db.models.User;
import db.services.AccountService;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;


public abstract class DBAccountServiceImpl implements AccountService{

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
    public boolean addUser(@NotNull User user) {
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
    public User getUser(@NotNull Long id) {
        throw new NotImplementedException();

    }

    @NotNull
    @Override
    public User getUser(@NotNull String username) {
        throw new NotImplementedException();

    }
}
