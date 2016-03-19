package db.services;


import db.models.User;
<<<<<<< HEAD
import db.models.game.ScoreTable;
=======
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9

import java.util.Collection;

public interface AccountService {
    Collection<User> getAllUsers();
<<<<<<< HEAD
    // method allows to define ID manually
    boolean addUser(Long userId, User user);
    // method id automatically and returns it after creation. 0 for fail
    Long addUser(User user);
    User getUser(Long userId);
    User getUser(String userName);
    Collection<ScoreTable> getuserScores();
=======

    boolean addUser(@NotNull Long userId, @NotNull User user);

    Long addUser(@NotNull User user);

    @Nullable
    User getUser(@NotNull Long userId);
    @Nullable
    User getUser(@NotNull String userName);
>>>>>>> db407bf416d8e59ce21af9e5ea10d275d0b3a3d9
}
