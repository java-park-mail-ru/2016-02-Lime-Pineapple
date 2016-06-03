package db.services.impl.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * created: 04-May-16
 * package: db.services.impl
 */
public class DBSessionFactoryService {
    public static final String DEFAULT_CONFIG_PATH = "hibernate.cfg.xml";
    public static final Logger LOGGER = LogManager.getLogger();

    SessionFactory sessionFactory;
    String configPath;

    public DBSessionFactoryService() {
        this.configPath = DEFAULT_CONFIG_PATH;
    }

    public DBSessionFactoryService(@NotNull String configPath) {
        this.configPath = configPath;
    }

    @NotNull
    public String getConfigPath() {
        return this.configPath;
    }


    public void configure(){
        if(sessionFactory == null) {
            LOGGER.info("[ I ] Building session factory...");
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure(configPath) // configures settings from hibernate.cfg.xml
                    .build();
            try {
                sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
            }
            catch (RuntimeException e) {
                // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
                // so destroy it manually.
                LOGGER.error(String.format("[ E ] Could not make DB session factory:%n%s", e.toString()));
                StandardServiceRegistryBuilder.destroy( registry );
                throw e;
            }
        }
    }

    @NotNull
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
