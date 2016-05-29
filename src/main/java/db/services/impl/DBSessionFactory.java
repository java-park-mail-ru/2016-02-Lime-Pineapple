package db.services.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * created: 04-May-16
 * package: db.services.impl
 */
public class DBSessionFactory {
    static SessionFactory sessionFactory;
    static boolean testDB = false;

    public static void enableTestDBMode() {
        testDB = true;
    }


    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure(testDB ? "hibernate.test.cfg.xml" : "hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (Exception e) {
                // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                // so destroy it manually.
                StandardServiceRegistryBuilder.destroy( registry );
            }
        }
        return sessionFactory;
    }

    public static Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }
}
