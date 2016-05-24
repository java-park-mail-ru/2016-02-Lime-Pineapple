package db.services.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;


public class DBUtilities {
    public static boolean deleteById(Session session, Class<?> type, Serializable id) {
        final Object persistentInstance = session.load(type, id);
        if (persistentInstance != null) {
            session.delete(persistentInstance);
            return true;
        }
        return false;
    }

    public static List<?> findByProperty(Session session, Class<?> type, String propertyName, Serializable value) {
        final Criteria criteria = session.createCriteria(type.getClass());
        return criteria.add(Restrictions.eq(propertyName, value)).list();
    }

    public static Object findUniqueByProperty(Session session, Class aClass, String propertyName, Serializable value) {
        final Criteria criteria = session.createCriteria(aClass);
        return criteria.add(Restrictions.eq(propertyName, value)).uniqueResult();
    }
}
