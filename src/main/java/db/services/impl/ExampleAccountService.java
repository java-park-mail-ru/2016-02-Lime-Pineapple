package db.services.impl;

import db.services.AccountService;
import db.models.User;
import net.sf.hibernate.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import java.io.Serializable;
//import java.sql.Connection;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//import java.lang.ref.WeakReference;
import java.util.Collection;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.hibernate.cfg.Configuration;

//import javax.naming.NamingException;
//import javax.naming.Reference;

public class ExampleAccountService implements AccountService {

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountService.class);
    private Map<String, User> users = new HashMap<>();
    private Map<Long, String> userids = new HashMap<>();
    private final int userlimit = 100000;
    private SessionFactory sessionFactory;

    public ExampleAccountService() {
        try {
            sessionFactory = new Configuration().addClass(User.class).buildSessionFactory();
        }
        catch (HibernateException e) {
            LOGGER.error("Erroe connecting to database");
            sessionFactory=null;
        }
        if (loadUsersFromDatabase()==0) {
            addUser(new User("admin@admin.ru", "admin"));
            addUser(new User("guest@mail.ru", "12345"));
            users.get("admin@admin.ru").increaseScore(10);
        }
        else LOGGER.error("User database loaded successfully");
    }
    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public boolean addUser(@NotNull Long userId, @NotNull User user) {

        try {
            if (this.users.containsKey(user.getLogin())) {
                LOGGER.error("User with this id already exists");
                return false;
            }
            else {
                users.put(user.getLogin(), user);
                return true;
            }

        } catch (RuntimeException e) {

            LOGGER.error("Error");
            return false;
        }
        
    }
    @Override
    public Long addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
        user.setId(value);
        if (this.addUser(value, user)) {
            userids.put(value, user.getLogin());
            try {
                if (saveUserToDatabase(user)) LOGGER.error("User added to database");
                else LOGGER.error("Failedtosaveuser");
            }
            catch (HibernateException e) {
                LOGGER.error("Failedtosaveuser");
            }
            return user.getId();
        }
        else {
            LOGGER.error("User was not registrated");
            return 0L;
        }

    }
    @Nullable @Override
    public User getUser(@NotNull Long userId) {

        return this.users.get(userids.get(userId));
    }
    @Nullable @Override
    public User getUser(@NotNull String userName) {
        //
        return this.users.get(userName);
    }
    @Override

    public Collection<String>getuserScores() {
        final Map<Integer, String> playerscores=new TreeMap<>();
        final Iterator<Map.Entry<String, User>>allusers=users.entrySet().iterator();
        while (allusers.hasNext()) {
            final User current=allusers.next().getValue();
            final String score=current.getLogin() + " scored " + current.getScore().toString();
            playerscores.put(current.getScore(), score);
        }
        return playerscores.values();
    }
    @Override
    public boolean changeUser(@NotNull User user)
    {
        if (users.containsKey(user.getLogin())) {
            users.put(user.getLogin(), user);
            return true;
        }
        else return false;
    }
    @Override
    public boolean removeUser(@NotNull Long userid) {
        if (userids.containsKey(userid)) {
            final String username = userids.get(userid);
            users.remove(username);
            userids.remove(userid);
            LOGGER.error("User "+username + " was deleted");
            return true;
        }
        else {
            LOGGER.error("User with id "+userid +" not found");
            return false;
        }
    }
    @Override
    public int getUsersLimit() {
        return userlimit;
    }
    @Override
    public int getUsersCount() {
        return users.size();
    }
    protected int loadUsersFromDatabase() {

        try {
            final List<User> usertable=getAll();
            if (usertable!=null) {
                while (usertable.iterator().hasNext()) {
                    final User user = usertable.iterator().next();
                    userids.put(user.getId(), user.getLogin());
                    users.put(user.getLogin(), user);
                }
                LOGGER.error("Userbase loaded");
                return 1;
            }
            else {
                LOGGER.error("Failed loading users");
                return 0;
            }
        }
        catch (HibernateException e){
            LOGGER.error("Failed loading users");
            return 0;
        }

    }
    protected boolean saveUserToDatabase(User user) throws HibernateException {

       if (sessionFactory!=null) {
            final Session saving = sessionFactory.openSession();
            final Transaction trans = saving.beginTransaction();
            saving.save(user);
            trans.commit();
            saving.close();
            return true;
        }
        else return false;
    }
    @Nullable
    protected List<User> getAll() throws HibernateException {

        if (sessionFactory!=null) {

            final Session loading = sessionFactory.openSession();
            final Criteria read = loading.createCriteria(User.class);
            return read.list();
        }
        else {
            return null;
        }
    }

}
