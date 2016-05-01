package db.services;


import db.models.User;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
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
    int getUsersLimit();
    int getUsersCount();
    boolean changeUser(@NotNull User user);
    //boolean testConnect() throws MappingException, HibernateException;
    Collection<User> getUsers();
    boolean hasUser(long id);
    boolean hasUser(@NotNull String username);
    boolean removeUser(long id);
    boolean removeUser(@NotNull String username);

}
