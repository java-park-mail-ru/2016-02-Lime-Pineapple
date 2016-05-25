package db.services.impl;
import db.models.User;
import db.services.SessionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SessionServiceImpl implements SessionService {

    private static long sessId=0L;
    private static Map<String, Long> authorizedUsers=new ConcurrentHashMap<>();
    @Override
    public long logIn(User user){

        if (authorizedUsers.get(user.getUsername())==null) {
            sessId=sessId+1;
            authorizedUsers.put(user.getUsername(),sessId);
            return sessId;
        }
        else return 0L;
    }
    @Override
    public long checkAuthorization(User user) {
        if (authorizedUsers.get(user.getUsername())!=null) {
            return authorizedUsers.get(user.getUsername());
        }
        else return  0L;
    }
    @Override
    public boolean logOut(User user) {
        authorizedUsers.remove(user.getUsername());
        return true;
    }
}
