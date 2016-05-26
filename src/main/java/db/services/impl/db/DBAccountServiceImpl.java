package db.services.impl.db;

import db.models.User;
import db.services.AccountService;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.AccessException;
import java.util.Collection;
import java.util.List;

/**
 * created: 04-May-16
 * package: db.services.impl
 */
public class DBAccountServiceImpl implements AccountService {
    DBSessionFactoryService sessionFactory;
    AccountDAO dao;

    public DBAccountServiceImpl(DBSessionFactoryService sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private interface AccessMethod<ReturnType, ParamType> {
        ReturnType get(Session session, @Nullable ParamType value);
    }

    public class HelperExecutor<ReturnType, ParamType> {
        public ReturnType accessObject(@Nullable ParamType value,
                                       AccessMethod<ReturnType, ParamType> method,
                                       String excString) throws AccessException {
            final Session session = sessionFactory.getCurrentSession();
            final Transaction tx = session.beginTransaction();
            ReturnType ret;
            try {
                ret = method.get(session, value);
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
        return new HelperExecutor<List<User>,Void>().accessObject(null, (session, value) -> {
            return this.dao.getUsers(session);
        }, "could not get users");
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
        return new HelperExecutor<Long, User>().accessObject(user, (session, value) -> {
            return this.dao.addUser(session, value);
        }, "could not get users");
    }

    @Override
    public boolean removeUser(long id) throws AccessException {
        return new HelperExecutor<Boolean, Long>().accessObject(id, (session, value) -> {
            return this.dao.removeUser(session, value);
        }, "could not get users");
    }

    @Override
    public boolean removeUser(@NotNull String username) {
        final Session session = this.sessionFactory.getCurrentSession();
        // TODO: fix this super slow method
        final Transaction tx = session.beginTransaction();
        final Object got = DBUtilities.findUniqueByProperty(session, User.class, "username", username);
        session.delete(got);
        tx.commit();
        return true;
    }

    @Override
    public User getUser(long id) throws AccessException {
        return new HelperExecutor<User, Long>().accessObject(id, (session, value) -> {
            return this.dao.getUser(session, value);
        }, "could not get users");
    }

    @Override
    public User getUser(@NotNull String username) throws AccessException {
        return new HelperExecutor<User, String>().accessObject(username, (session, value) -> {
            return this.dao.getUser(session, value);
        }, "could not get users");
    }

    @Override
    public int getCount() throws AccessException {
        return new HelperExecutor<Integer, Long>().accessObject(null, (session, value) -> {
            return this.dao.getCount(session).intValue();
        }, "Could not get count");
    }

    @Override
    public boolean changeUser(@NotNull User user) {
        return false;
    }

    @Override
    public void clear() {
        final Session session = this.sessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();

        // TODO change it to fetch db name from entity
        final String hql = String.format("delete from %s","User");
        final Query query = session.createQuery(hql);
        query.executeUpdate();
        tx.commit();

    }


}
