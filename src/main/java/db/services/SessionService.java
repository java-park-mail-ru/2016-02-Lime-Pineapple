package db.services;

import db.models.User;

public interface SessionService {
    long logIn(User user);
    long checkAuthorization(User user);
    boolean logOut(User user);
}
