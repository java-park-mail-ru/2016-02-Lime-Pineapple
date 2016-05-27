package db.services;

import db.models.User;
import db.models.UserScore;
import db.services.impl.db.AccountDAO;
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
import java.util.Objects;
import java.util.Vector;

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

    private void pnh(AccessException e) {
        fail(String.format("AccessException in Test: %n%s", e.toString()));
    }

    public DBAccountServiceImpl prepareDB() {
        final DBSessionFactoryService sessionFactory = new DBSessionFactoryService("hibernate.test.cfg.xml");
        sessionFactory.configure();
        final DBAccountServiceImpl dba = new DBAccountServiceImpl(sessionFactory, new AccountDAO());
        try {
            dba.clear();
        } catch (AccessException e) {
            pnh(e);
        }

        return dba;
    }

    public User createAndSelectRandom(DBAccountServiceImpl dba) throws AccessException {
        final List<User> ids = new ArrayList<>();
        for(int i = 1; i<=100; ++i) {
            final User user = new User(String.format("test%d",i), String.format("test%dPassword",i));
            user.setScore((int) Math.round(Math.random()*100));
            user.setPlayedGames((int)Math.round(Math.random()*100));
            user.setBestScore((int)Math.round(Math.random()*20));
            dba.addUser(user);
            ids.add(user);
        }
        final Long selectedId = Math.round(Math.floor(Math.random()*100));
        return ids.get(selectedId.intValue());
    }

    @Test
    public void testClear() {
        final DBAccountServiceImpl dba = prepareDB();
        try {
            for(int i = 0; i<10; ++i) {
                dba.addUser(new User(String.format("test%d",i), "test"));
            }
            dba.clear();
            assertEquals("Users count is not 0", 0, dba.getCount());
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testGetCount() {
        final DBAccountServiceImpl dba = prepareDB();
        try {
            for(int i = 0; i<10; ++i) {
                dba.addUser(new User(String.format("test%d",i), "test"));
            }
            assertEquals("Actual user count does not match creation count", 10, dba.getCount());
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testAddUser() {
        final DBAccountServiceImpl dba = prepareDB();
        final User created = new User("TESTOVIY_USER", "VAHPAROL123");
        try {
            final Long id = dba.addUser(created);
            final User actual = dba.getUser(id);
            assertTrue("Actual user from db does not match created one",
                    created.getUsername().equals(actual.getUsername()) && created.getPassword().equals(actual.getPassword()));
        } catch (AccessException e) {
            pnh(e);
        }
    }




    @Test
    public void testGetById() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            final User user = createAndSelectRandom(dba);
            final User actualUser = dba.getUser(user.getId());
            assertEquals("Actual user from db does not match created one",
                    user, actualUser);
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testHasUserById() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            final User user = createAndSelectRandom(dba);
            assertTrue("DBA hasUser() returned false", dba.hasUser(user.getId()));
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testHasUserByUsername()  {
        final DBAccountServiceImpl dba = prepareDB();
        try {
            final User user = createAndSelectRandom(dba);
            assertTrue("DBA hasUser() returned false", dba.hasUser(user.getUsername()));
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testGetByUsername() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            final User user = createAndSelectRandom(dba);
            final User actualUser = dba.getUser(user.getUsername());
            assertEquals("Actual user from db does not match created one",
                    user, actualUser);
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testRemoveById() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            final User user = createAndSelectRandom(dba);
            dba.removeUser(user.getId());
            assertNull(dba.getUser(user.getId()));
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testRemoveByUsername() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            final User user = createAndSelectRandom(dba);
            dba.removeUser(user.getUsername());
            assertNull(dba.getUser(user.getUsername()));
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @SuppressWarnings("TooBroadScope")
    @Test
    public void testChangeUser() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            User user = createAndSelectRandom(dba);
            final User modelUser = new User(user);
            final int newScore = 1000;
            final String newName = "LOSHARA_TEST";

            modelUser.setScore(newScore);
            modelUser.setUsername(newName);

            user.setUsername(newName);
            user.setScore(newScore);

            dba.changeUser(user);
            user = dba.getUser(newName);
            assertEquals("Local and remote users are not the same", modelUser, user);
        } catch (AccessException e) {
            pnh(e);
        }
    }


    @Test
    public void testGetScores() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            createAndSelectRandom(dba);
            final List<UserScore> uss = (List<UserScore>) dba.getScores();
            assertEquals(dba.getCount(), uss.size());
            for (UserScore us : uss) {
                final User modelUser = dba.getUser(us.getUsername());
                assertNotNull(modelUser);
                final UserScore modelUs = new UserScore(modelUser);
                assertEquals(
                        "Model UserScore (got from username of actual score itself) is not the same as actual one",
                        modelUs, us);
            }
        } catch (AccessException e) {
            pnh(e);
        }
    }

    @Test
    public void testSortingInGetScores() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            createAndSelectRandom(dba);
            final List<UserScore> userScoresActual = (List<UserScore>) dba.getScores();
            final List<UserScore> userScoresSortModel = new Vector<>(userScoresActual);
            userScoresSortModel.sort( (UserScore a, UserScore b) -> {
                if (!Objects.equals(a.getScore(), b.getScore()))
                    return b.getScore() - a.getScore();
                else if (!Objects.equals(a.getBestScore(), b.getBestScore()))
                    return b.getBestScore() - a.getBestScore();
                else
                    return b.getPlayedGames() - a.getPlayedGames();
            });
            for (int i = 0, s = userScoresActual.size(); i<s; ++i) {
                assertEquals(
                        "Sorting probably sucks. Mismatch in actual and model sorting",
                        userScoresSortModel.get(i), userScoresActual.get(i));
            }
        } catch (AccessException e) {
            pnh(e);
        }
    }

    // Full test
    @Test
    public void testHibernateEntityUser() {
        try {
            final DBAccountServiceImpl dba = prepareDB();
            final User testUser = new User("test", "test");
            final long testId = dba.addUser(testUser);
            assertTrue("Account service couldn't create user in database", testId != 0);
            assertTrue("Account service reports that test user does not exists", dba.hasUser(testId));
            dba.removeUser(testId);
        } catch (AccessException e) {
            pnh(e);
        }
    }

}
