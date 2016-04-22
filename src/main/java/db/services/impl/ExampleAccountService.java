package db.services.impl;

import db.models.validation.ValidationException;
import db.services.AccountService;
import db.models.User;
import net.sf.hibernate.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.util.*;

import org.hibernate.MappingException;
//import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;
import net.sf.hibernate.cfg.Configuration;

public class ExampleAccountService implements AccountService {

    private AtomicLong autoIncrementId = new AtomicLong(0L);
    private static final Logger LOGGER = LogManager.getLogger(ExampleAccountService.class);
    private Map<String, User> users = new HashMap<>();
    private Map<Long, String> userids = new HashMap<>();
    private static final int USERLIMIT = 100000;
    private SessionFactory sessionFactory;

    public ExampleAccountService() {
        try {
            connectToDatabase();
        }
        catch (MappingException e) {
            LOGGER.fatal("Error connecting to database"+e.getMessage());
            sessionFactory=null;
        }
        catch (HibernateException e1) {
            LOGGER.fatal("Error reading database"+e1.getMessage());
            sessionFactory=null;
        }
        if (loadUsersFromDatabase()==0) {
            try {
                addUser(new User("admin@admin.ru", "admin"));
                users.get("admin@admin.ru").increaseScore(10);
                addUser(new User("guest@mail.ru", "5"));
            }
            catch (ValidationException e) {
                LOGGER.error(e.getMessage());
            }
        }
        else LOGGER.info("User database loaded successfully");
    }
    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public boolean addUser(@NotNull Long userId, @NotNull User user) {

        try {
            if (this.users.containsKey(user.getLogin())) {
                LOGGER.error("User with this name already exists");
                return false;
            }
            else {
                try {
                    final User newUser = new User(user.getLogin(), user.getPassword());
                    newUser.setId(userId);
                    users.put(user.getLogin(), newUser);
                }
                catch (ValidationException e) {
                    LOGGER.error("Invalid data");
                }
                return true;
            }

        } catch (RuntimeException e) {

            LOGGER.error("Error");
            return false;
        }
        
    }
    @Override
    public boolean addUser(@NotNull User user) {
        final Long value = this.autoIncrementId.incrementAndGet();
        user.setId(value);
        if (this.addUser(value, user)) {
            userids.put(value, user.getLogin());
            try {
                if (saveUserToDatabase(user)) LOGGER.info("User added to database");
                else LOGGER.error("Failedtosaveuser");
            }
            catch (HibernateException e) {
                LOGGER.error("Failedtosaveuser");
            }
            return true;
        }
        else {
            LOGGER.error("User was not registrated");
            return false;
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
    public Collection<String>getUserScores() {
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
            try {
                final User updatingUser = users.get(user.getLogin());
                updatingUser.setPassword(user.getPassword());
                updatingUser.setNickname(user.getNickname());
                users.put(user.getLogin(), updatingUser);
                LOGGER.info("User was updated");
                return true;
            }
            catch (ValidationException e) {
                LOGGER.error("User was not updated");
                return false;
            }
        }
        else return false;
    }
    @Override
    public boolean removeUser(long userid) {
        if (userids.containsKey(userid)) {
            final String username = userids.get(userid);
            users.remove(username);
            userids.remove(userid);
            LOGGER.info("User "+username + " was deleted");
            return true;
        }
        else {
            LOGGER.error("User with id "+userid +" not found");
            return false;
        }
    }
    @Override
    public int getUsersLimit() {
        return USERLIMIT;
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
    private void connectToDatabase() throws MappingException, HibernateException{
        final Configuration config=new Configuration();
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.connection.drivet_class", "com.mysql.jdbc.Driver");
        config.setProperty("hibernate.connection.url","jdbc:mysql://localhosdsgdt:3306/WHACKAMOLEUSERS");
        config.setProperty("hibernate.connection.username", "root");
        config.setProperty("hibernate.connection.password", "yyvt9z3e");
        config.setProperty("hibernate.show_sql", "true");
        config.setProperty("hibernate.hbm2dll", "update");
        config.addResource("/src/main/java/db/models/User.hbm.xml", User.class.getClassLoader());
        sessionFactory = config.addClass(User.class).buildSessionFactory();
    }
    public boolean testConnect() {
        try {
            connectToDatabase();
            return true;
        }
        catch (MappingException e) {
            return false;
        }
        catch (HibernateException e) {
            return false;
        }
    }
    public boolean hasUser(@NotNull String username) {
        return false;
    }
    @Override
    public boolean hasUser(long id) {
        return false;
    }
    @Override
    public boolean removeUser(@NotNull String username) {
        if(this.hasUser(username)) {
            final User user  = this.users.remove(username);
            this.userids.remove(user.getId());
            return true;
        }
        return false;
    }


}
