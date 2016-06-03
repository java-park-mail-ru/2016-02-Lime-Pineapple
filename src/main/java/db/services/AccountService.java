package db.services;


import db.exceptions.DatabaseException;
import db.models.User;

import db.models.UserScore;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public interface AccountService {
    Collection<User> getUsers() throws DatabaseException;

    boolean hasUser(long id) throws DatabaseException;
    boolean hasUser(@NotNull String username) throws DatabaseException;

    long addUser(@NotNull User user) throws DatabaseException;

    boolean removeUser(long id) throws DatabaseException;
    boolean removeUser(@NotNull String username) throws DatabaseException;

    User getUser(long id) throws DatabaseException;
    User getUser(@NotNull String username) throws DatabaseException;

    int getCount() throws DatabaseException;
    boolean changeUser(@NotNull User user) throws DatabaseException;

    void clear() throws DatabaseException;

    Collection<UserScore> getScores() throws DatabaseException;


    void updateUserStats(@NotNull User user, @NotNull Integer newScore) throws DatabaseException;


}
