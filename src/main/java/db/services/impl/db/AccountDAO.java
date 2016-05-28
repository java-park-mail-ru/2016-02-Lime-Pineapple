package db.services.impl.db;

import db.models.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * created: 5/26/2016
 * package: db.services.impl
 */
public class AccountDAO {

    @SuppressWarnings("unchecked")
    List<User> getUsers(@NotNull Session session) {
        return session.createCriteria(User.class).list();
    }

    User getUser(@NotNull Session session,@NotNull Long id) {
        return session.get(User.class, id);
    }

    @Nullable
    User getUser(@NotNull Session session,@NotNull String username) {
        final Criteria criteria = session.createCriteria(User.class);
        final Object user = criteria.add(Restrictions.eq("username", username))
                .uniqueResult();
        return (User)user;
    }

    void clear(@NotNull Session session) {
        final String hql = String.format("delete from %s","User");
        final Query query = session.createQuery(hql);
        query.executeUpdate();
    }

    long addUser(@NotNull Session session,@NotNull User user) {
        session.save(user);
        return user.getId();
    }

    boolean removeUser(@NotNull Session session,@NotNull Long id) {
        return DBUtilities.deleteById(session, User.class, id);
    }

    boolean removeUser(@NotNull Session session,@NotNull String username) {
        final Object got = DBUtilities.findUniqueByProperty(session, User.class, "username", username);
        session.delete(got);
        return true;
    }

    Long getCount(@NotNull Session session) {
        return (Long)session.createCriteria(User.class).setProjection(Projections.rowCount()).uniqueResult();
    }

    boolean changeUser(@NotNull Session session, @NotNull User user) {
        session.update(user);
        return true;
    }

    @SuppressWarnings("unchecked")
    List<User> getSortedUsers(@NotNull Session session) {
        final Criteria criteria = session.createCriteria(User.class);
        criteria.addOrder(Order.desc("score"));
        criteria.addOrder(Order.desc("bestScore"));
        criteria.addOrder(Order.desc("playedGames"));
        return (List<User>)criteria.list();
    }
}
