package db.services;


import db.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.AccessException;
import java.util.Collection;

public interface AccountService {
    Collection<User> getUsers() throws AccessException;

    boolean hasUser(long id) throws AccessException;
    boolean hasUser(@NotNull String username) throws AccessException;

    long addUser(@NotNull User user) throws AccessException;

    boolean removeUser(long id) throws AccessException;
    boolean removeUser(@NotNull String username);

    User getUser(long id) throws AccessException;
    User getUser(@NotNull String username) throws AccessException;

    int getCount() throws AccessException;
    boolean changeUser(@NotNull User user);

    void clear();

}
