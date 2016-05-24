package db.services.impl;

import db.models.User;
import db.services.AccountService;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;


public class DBAccountServiceImpl implements AccountService {

    @SuppressWarnings("unchecked")
    @Nullable
    @Override

    public Collection<User> getUsers() {
        List<User> users;
        try {
            final Session session = DBSessionFactory.getCurrentSession();
            final Transaction tx = session.beginTransaction();
            users = session.createCriteria(User.class).list();
            tx.commit();
        }
        catch (HibernateException e) {
            users=null;
        }
        return users;
    }

    @Override
    public boolean hasUser(long id) {
        return getUser(id) != null;
    }

    @Override
    public boolean hasUser(@NotNull String username) {
        return getUser(username) != null;
    }

    @Override
    public long addUser(@NotNull User user) {
        if (validateUser(user)) {
            final Session session = DBSessionFactory.getCurrentSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return user.getId();
        }
        else return 0L;
    }

    @Override
    public boolean removeUser(long id) {
        final Session session = DBSessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final boolean result = DBUtilities.deleteById(session, User.class, id);
        tx.commit();
        return result;
    }

    @Override
    public boolean removeUser(@NotNull String username) {
        final Session session = DBSessionFactory.getCurrentSession();
        // TODO: fix this super slow method
        final Transaction tx = session.beginTransaction();
        final Object got = DBUtilities.findUniqueByProperty(session, User.class, "username", username);
        session.delete(got);
        tx.commit();
        return true;
    }

    @Override
    public User getUser(long id) {
        final Session session = DBSessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final User user = session.get(User.class, id);
        tx.commit();
        return user;
    }

    @Override
    public User getUser(@NotNull String username) {
        final Session session = DBSessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final Criteria criteria = session.createCriteria(User.class);
        final Object user = criteria.add(Restrictions.eq("username", username))
                .uniqueResult();
        tx.commit();
        return (User)user;
    }

    @Override
    public int getCount() {
        final Session session = DBSessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();
        final Long count = (Long)session.createCriteria(User.class).setProjection(Projections.rowCount()).uniqueResult();
        tx.commit();
        return count.intValue();
    }

    @Override
    public boolean changeUser(@NotNull User user) {
        return false;
    }

    @Override
    public void clear() {
        final Session session = DBSessionFactory.getCurrentSession();
        final Transaction tx = session.beginTransaction();
        // TODO change it to fetch db name from entity
        final String hql = String.format("delete from %s","User");
        final Query query = session.createQuery(hql);
        query.executeUpdate();
        tx.commit();
    }
    private boolean validateUser(User user) {

        return (getUser(user.getUsername())==null || user.getPassword().length()>=4);
    }
}
