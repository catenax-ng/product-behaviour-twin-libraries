package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database;

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
        basePackages = "net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database",
        entityManagerFactoryRef = "rawdataEntityManager",
        transactionManagerRef = "rawdataTransactionManager"
)
public class PersistenceRawdataConfiguration {
    public static final String TRANSACTION_MANAGER = "rawdataTransactionManager";

    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean rawdataEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(rawdataDataSource());
        em.setPackagesToScan(new String[] { "net.catena_x.btp.libraries.oem.backend.database.rawdata.model" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(DIALECT, env.getProperty("rawdatadb.hibernate.dialect"));
        properties.put(SHOW_SQL, env.getProperty("rawdatadb.show-sql"));
        properties.put(HBM2DDL_AUTO, env.getProperty("rawdatadb.hibernate.hbm2ddl.auto"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource rawdataDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("rawdatadb.drivername"));
        dataSource.setUrl(env.getProperty("rawdatadb.url"));
        dataSource.setUsername(env.getProperty("rawdatadb.username"));
        dataSource.setPassword(env.getProperty("rawdatadb.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager rawdataTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(rawdataEntityManager().getObject());
        return transactionManager;
    }
}
