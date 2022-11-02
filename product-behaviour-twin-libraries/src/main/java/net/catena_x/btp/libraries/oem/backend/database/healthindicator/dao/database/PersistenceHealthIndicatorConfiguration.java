package net.catena_x.btp.libraries.oem.backend.database.healthindicator.dao.database;

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

import javax.sql.DataSource;
import java.util.HashMap;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@PropertySource({ "classpath:healthindicatordb.properties" })
@EnableJpaRepositories(
        basePackages = "net.catena_x.btp.libraries.oem.backend.database.healthindicator.dao.database",
        entityManagerFactoryRef = "healthindicatorEntityManager",
        transactionManagerRef = "healthindicatorTransactionManager"
)

public class PersistenceHealthIndicatorConfiguration {
    @Autowired private Environment environment;

    @Bean
    public LocalContainerEntityManagerFactoryBean healthindicatorEntityManager() {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(healthindicatorDataSource());
        entityManager.setPackagesToScan(
                new String[] { "net.catena_x.btp.libraries.oem.backend.database.healthindicator.model" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(DIALECT, environment.getProperty("healthindicatordb.hibernate.dialect"));
        properties.put(SHOW_SQL, environment.getProperty("healthindicatordb.show-sql"));
        properties.put(HBM2DDL_AUTO, environment.getProperty("healthindicatordb.hibernate.hbm2ddl.auto"));

        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean
    public DataSource healthindicatorDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("healthindicatordb.drivername"));
        dataSource.setUrl(environment.getProperty("healthindicatordb.url"));
        dataSource.setUsername(environment.getProperty("healthindicatordb.username"));
        dataSource.setPassword(environment.getProperty("healthindicatordb.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager healthindicatorTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(healthindicatorEntityManager().getObject());
        return transactionManager;
    }
}
