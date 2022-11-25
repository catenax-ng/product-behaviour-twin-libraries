package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config;

import org.springframework.beans.factory.annotation.Autowired;
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

import javax.sql.DataSource;
import java.util.HashMap;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource({ "classpath:rawdatadb.properties" })
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables",
        entityManagerFactoryRef = "rawdataEntityManager",
        transactionManagerRef = "rawdataTransactionManager"
)
public class PersistenceRawdataConfiguration {
    public static final String TRANSACTION_MANAGER = "rawdataTransactionManager";

    @Autowired private Environment environment;

    @Bean
    public LocalContainerEntityManagerFactoryBean rawdataEntityManager() {
        final LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(rawdataDataSource());
        entityManager.setPackagesToScan(
                "net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables");

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        final HashMap<String, Object> properties = new HashMap<>();
        properties.put(DIALECT, environment.getProperty("rawdatadb.hibernate.dialect"));
        properties.put(SHOW_SQL, environment.getProperty("rawdatadb.show-sql"));
        properties.put(HBM2DDL_AUTO, environment.getProperty("rawdatadb.hibernate.hbm2ddl.auto"));

        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean
    public DataSource rawdataDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("rawdatadb.drivername"));
        dataSource.setUrl(environment.getProperty("rawdatadb.url"));
        dataSource.setUsername(environment.getProperty("rawdatadb.username"));
        dataSource.setPassword(environment.getProperty("rawdatadb.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager rawdataTransactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(rawdataEntityManager().getObject());

        return transactionManager;
    }
}
