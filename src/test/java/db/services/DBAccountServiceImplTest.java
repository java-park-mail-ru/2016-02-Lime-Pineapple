package db.services;

import db.models.User;
import db.services.impl.db.DBAccountServiceImpl;
import db.services.impl.db.DBSessionFactoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Test;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static junit.framework.TestCase.fail;

/**
 * created: 01-Apr-16
 * package: db.services
 */
public class DBAccountServiceImplTest {
    static final Logger LOGGER = LogManager.getLogger();

    @Test
    public void testSmokeHibernateConfiguration() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        new MetadataSources( registry ).buildMetadata().buildSessionFactory();

    }

    @Test
    public void testSmokeHibernateSession() {
        LOGGER.info("[ T ] Begin testing hibernate session...");
        Session session = null;
        try {
            final DBSessionFactoryService sessionFactory = new DBSessionFactoryService();
            sessionFactory.configure();
            session = sessionFactory.getCurrentSession();
            LOGGER.info("[ ok ] Session created successfully!");
        } catch (RuntimeException e) {
            fail(e.toString());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public DBAccountServiceImpl prepareDB() throws AccessException {
        final DBSessionFactoryService sessionFactory = new DBSessionFactoryService("hibernate.test.cfg.xml");
        sessionFactory.configure();
        final DBAccountServiceImpl dba = new DBAccountServiceImpl(sessionFactory);
        dba.clear();
        return dba;
    }

    @Test
    public void testClear() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        for(int i = 0; i<10; ++i) {
            dba.addUser(new User(String.format("test%d",i), "test"));
        }
        dba.clear();
        assertEquals("Users count is not 0", 0, dba.getCount());
    }

    @Test
    public void testGetCount() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        for(int i = 0; i<10; ++i) {
            dba.addUser(new User(String.format("test%d",i), "test"));
        }
        assertEquals("Actual user count does not match creation count", 10, dba.getCount());
    }

    @Test
    public void testAddUser() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User created = new User("TESTOVIY_USER", "VAHPAROL123");
        final Long id = dba.addUser(created);
        final User actual = dba.getUser(id);
        assertTrue("Actual user from db does not match created one",
                created.getUsername().equals(actual.getUsername()) && created.getPassword().equals(actual.getPassword()));
    }

    public User createAndSelectRandom(DBAccountServiceImpl dba) throws AccessException {
        List<User> ids = new ArrayList<>();
        for(int i = 1; i<=100; ++i) {
            User user = new User(String.format("test%d",i), String.format("test%dPassword",i));
            dba.addUser(user);
            ids.add(user);
        }
        final Long selectedId = Math.round(Math.floor(Math.random()*100));
        return ids.get(selectedId.intValue());
    }


    @Test
    public void testGetById() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User user = createAndSelectRandom(dba);
        final User actualUser = dba.getUser(user.getId());
        assertEquals("Actual user from db does not match created one",
               user, actualUser);
    }

    @Test
    public void testHasUserById() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User user = createAndSelectRandom(dba);
        assertTrue("DBA hasUser() returned false", dba.hasUser(user.getId()));
    }

    @Test
    public void testHasUserByUsername() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User user = createAndSelectRandom(dba);
        assertTrue("DBA hasUser() returned false", dba.hasUser(user.getUsername()));
    }

    @Test
    public void testGetByUsername() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User user = createAndSelectRandom(dba);
        final User actualUser = dba.getUser(user.getUsername());
        assertEquals("Actual user from db does not match created one",
                user, actualUser);
    }

    @Test
    public void testRemoveById() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User user = createAndSelectRandom(dba);
        dba.removeUser(user.getId());
        assertNull(dba.getUser(user.getId()));
    }

    @Test
    public void testRemoveByUsername() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User user = createAndSelectRandom(dba);
        dba.removeUser(user.getUsername());
        assertNull(dba.getUser(user.getUsername()));
    }


    // Full test
    @Test
    public void testHibernateEntityUser() throws AccessException {
        final DBAccountServiceImpl dba = prepareDB();
        final User testUser = new User("test", "test");
        final long testId = dba.addUser(testUser);
        assertTrue("Account service couldn't create user in database", testId != 0);
        assertTrue("Account service reports that test user does not exists", dba.hasUser(testId));

        dba.removeUser(testId);
    }

}
