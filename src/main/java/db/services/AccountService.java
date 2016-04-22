package db.services;


import db.models.User;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface AccountService {


        // method id automatically and returns it after creation. 0 for fail
    Collection<String> getUserScores();
    boolean addUser(@NotNull Long userId, @NotNull User user);
    boolean addUser(@NotNull User user);
    @Nullable
    User getUser(@NotNull Long userId);
    @Nullable
    User getUser(@NotNull String userName);
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
