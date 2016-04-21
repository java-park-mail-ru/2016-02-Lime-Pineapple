package db.services;


import db.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;



public interface AccountService {
    Collection<User> getUsers();

    boolean hasUser(long id);
    boolean hasUser(@NotNull String username);

    long addUser(@NotNull User user);

    boolean removeUser(long id);
    boolean removeUser(@NotNull String username);

    @NotNull User getUser(long id);
    @NotNull User getUser(@NotNull String username);
}
