package db.services.impl.db;

import db.models.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * created: 5/26/2016
 * package: db.services.impl
 */
public class AccountDAO {

    @SuppressWarnings("unchecked")
    List<User> getUsers(Session session) {
        return session.createCriteria(User.class).list();
    }

    User getUser(Session session, Long id) {
        return session.get(User.class, id);
    }

    User getUser(Session session, String username) {
        return session.get(User.class, username);
    }

    void clear(Session session) {
        final String hql = String.format("delete from %s","User");
        final Query query = session.createQuery(hql);
        query.executeUpdate();
    }

    long addUser(Session session, User user) {
        session.save(user);
        return user.getId();
    }

    boolean removeUser(Session session, Long id) {
        return DBUtilities.deleteById(session, User.class, id);
    }

    Long getCount(Session session) {
        return (Long)session.createCriteria(User.class).setProjection(Projections.rowCount()).uniqueResult();
    }
}
