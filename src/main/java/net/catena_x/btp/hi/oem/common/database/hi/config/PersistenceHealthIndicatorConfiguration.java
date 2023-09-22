package net.catena_x.btp.hi.oem.common.database.hi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource({"classpath:healthindicatordb.properties"})
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {PersistenceHealthIndicatorConfiguration.BASE_PACKAGES},
        entityManagerFactoryRef = PersistenceHealthIndicatorConfiguration.ENTITY_MANAGER_FACTORY,
        transactionManagerRef = PersistenceHealthIndicatorConfiguration.TRANSACTION_MANAGER)
public class PersistenceHealthIndicatorConfiguration {
    public static final String PREFIX = "healthindicator";
    public static final String CONFIG_PREFIX = "healthindicatordb.";
    public static final String BASE_PACKAGES = "net.catena_x.btp.hi.oem.common.database.hi.tables";

    public static final String ENTITY_MANAGER_FACTORY = PREFIX + "EntityManagerFactory";
    public static final String ENTITY_MANAGER_FACTORY_BUILDER = ENTITY_MANAGER_FACTORY + "Builder";
    public static final String DATASOURCE = PREFIX + "DataSource";
    public static final String TRANSACTION_MANAGER = PREFIX + "TransactionManager";
    public static final String UNIT_NAME = PREFIX + "PU";

    @Autowired private Environment environment;

    @Bean(name = ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(ENTITY_MANAGER_FACTORY_BUILDER) EntityManagerFactoryBuilder builder,
            @Qualifier(DATASOURCE) DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .properties(buildProperties())
                .packages(BASE_PACKAGES)
                .persistenceUnit(UNIT_NAME)
                .build();
    }

    @Bean(name = DATASOURCE)
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty(CONFIG_PREFIX + "drivername"));
        dataSource.setUrl(environment.getProperty(CONFIG_PREFIX + "url"));
        dataSource.setUsername(environment.getProperty(CONFIG_PREFIX + "username"));
        dataSource.setPassword(environment.getProperty(CONFIG_PREFIX + "password"));

        return dataSource;
    }

    @Bean(name = TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier(ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = ENTITY_MANAGER_FACTORY_BUILDER)
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),
                new HashMap<>(), null);
    }

    private HashMap<String, Object> buildProperties() {
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put(DIALECT, environment.getProperty(CONFIG_PREFIX + "hibernate.dialect"));
        properties.put(HBM2DDL_AUTO, environment.getProperty(CONFIG_PREFIX + "hibernate.hbm2ddl.auto"));
        properties.put(SHOW_SQL, environment.getProperty(CONFIG_PREFIX + "show-sql"));
        return properties;
    }
}
