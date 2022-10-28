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
    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean healthindicatorEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(healthindicatorDataSource());
        em.setPackagesToScan(new String[] { "net.catena_x.btp.libraries.oem.backend.database.healthindicator.model" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(DIALECT, env.getProperty("healthindicatordb.hibernate.dialect"));
        properties.put(SHOW_SQL, env.getProperty("healthindicatordb.show-sql"));
        properties.put(HBM2DDL_AUTO, env.getProperty("healthindicatordb.hibernate.hbm2ddl.auto"));

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource healthindicatorDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("healthindicatordb.drivername"));
        dataSource.setUrl(env.getProperty("healthindicatordb.url"));
        dataSource.setUsername(env.getProperty("healthindicatordb.username"));
        dataSource.setPassword(env.getProperty("healthindicatordb.password"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager healthindicatorTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(healthindicatorEntityManager().getObject());
        return transactionManager;
    }
}
