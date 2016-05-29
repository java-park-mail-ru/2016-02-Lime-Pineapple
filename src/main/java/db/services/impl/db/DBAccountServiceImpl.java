package db.services.impl.db;

import db.models.User;
import db.models.UserScore;
import db.services.AccountService;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * created: 04-May-16
 * package: db.services.impl
 */
public class DBAccountServiceImpl implements AccountService {
    DBSessionFactoryService sessionFactory;
    AccountDAO dao;

    public DBAccountServiceImpl(DBSessionFactoryService sessionFactory, AccountDAO dao) {
        this.sessionFactory = sessionFactory;
        this.dao = dao;
    }

    private interface AccessMethod<ReturnType, ParamType> {
        @Nullable
        ReturnType get(Session session, @Nullable ParamType value);
    }

    public class HelperExecutor<ReturnType, ParamType> {
        public ReturnType accessObject(@Nullable ParamType value,
                                       AccessMethod<ReturnType, ParamType> method,
                                       String excString) throws AccessException {
            final Session session = sessionFactory.getCurrentSession();
            final Transaction tx = session.beginTransaction();
            try {
                final ReturnType ret = method.get(session, value);
                tx.commit();
                return ret;
            } catch (Throwable ignored) {
                if (tx.getStatus() == TransactionStatus.ACTIVE ){
                    tx.rollback();
                }
                throw new AccessException(
                        String.format(
                                "Exception while trying to access database:%n%s%nException:%n%s",
                                excString, ignored.toString()));
            }
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public Collection<User> getUsers() throws AccessException {
        return new HelperExecutor<List<User>,Void>().accessObject(null,
                (session, value) -> this.dao.getUsers(session), "Could not get user list");
    }

    @Override
    public boolean hasUser(long id) throws AccessException {
        return getUser(id) != null;
    }

    @Override
    public boolean hasUser(@NotNull String username) throws AccessException {
        return getUser(username) != null;
    }

    @Override
    public long addUser(@NotNull User user) throws AccessException {
        return new HelperExecutor<Long, User>().accessObject(user,
                (session, value) -> this.dao.addUser(session, value), "Could not add user");
    }

    @Override
    public boolean removeUser(long id) throws AccessException {
        return new HelperExecutor<Boolean, Long>().accessObject(id,
                (session, value) -> this.dao.removeUser(session, value), "Could not delete User by id");
    }

    @Override
    public boolean removeUser(@NotNull String username) throws AccessException {
        return new HelperExecutor<Boolean, String>().accessObject(username,
                (session, value) -> this.dao.removeUser(session, value), "Could not delete User by username");
    }

    @Override
    @Nullable
    public User getUser(long id) throws AccessException {
        return new HelperExecutor<User, Long>().accessObject(id,
                (session, value) -> this.dao.getUser(session, value), "Could not get User by id");
    }

    @Override
    @Nullable
    public User getUser(@NotNull String username) throws AccessException {
        return new HelperExecutor<User, String>().accessObject(username,
                (session, value) -> this.dao.getUser(session, value), "Could not get User by username");
    }

    @Override
    public int getCount() throws AccessException {
        return new HelperExecutor<Integer, Long>().accessObject(null,
                (session, value) -> this.dao.getCount(session).intValue(), "Could not get number of Users");
    }

    @Override
    public boolean changeUser(@NotNull User user) throws AccessException {
        new HelperExecutor<Boolean, User>().accessObject(user,
                (session, value) -> this.dao.changeUser(session, value), "Could not change User");
        return false;
    }

    @Override
    public void clear() throws AccessException {
        new HelperExecutor<Void, Void>().accessObject(null, (session, value) -> {
            this.dao.clear(session);
            return null;
        }, "Could not clear User table");

    }

    public Collection<UserScore> getScores() throws AccessException {
        return new HelperExecutor<Collection<UserScore>, Void>().accessObject(null,
                (session, value) -> {
                    final List<User> users = this.dao.getSortedUsers(session);
                    final List<UserScore> scores = new Vector<>(users.size());
                    //noinspection ForLoopReplaceableByForEach
                    for(int i = 0, s = users.size(); i<s; i++) {
                        scores.add(new UserScore(users.get(i)));
                    }
                    return scores;
                },
                "Could not get user scores");
    }

}
