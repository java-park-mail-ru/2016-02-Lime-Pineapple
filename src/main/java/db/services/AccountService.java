package db.services;


import db.models.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AccountService {
    Collection<User> getAllUsers();
        // method id automatically and returns it after creation. 0 for fail
    Collection<String> getuserScores();
    boolean addUser(@NotNull Long userId, @NotNull User user);
    Long addUser(@NotNull User user);
    @Nullable
    User getUser(@NotNull Long userId);
    @Nullable
    User getUser(@NotNull String userName);
    boolean removeUser(@NotNull String username);
    int getUsersLimit();
    int getUsersCount();

}
