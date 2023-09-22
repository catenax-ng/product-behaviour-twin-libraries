package net.catena_x.btp.libraries.oem.backend.datasource.testdatagenerator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {
        "net.catena_x.btp.libraries.notification",
        "net.catena_x.btp.libraries.util",
        "net.catena_x.btp.libraries.util.security",
        "net.catena_x.btp.libraries.oem.backend.testdatagenerator",
        "net.catena_x.btp.libraries.oem.backend.testdata",
        "net.catena_x.btp.libraries.oem.backend.model",
        "net.catena_x.btp.libraries.bamm.testdata"})
@OpenAPIDefinition(info = @Info(title = "Testdata generator for RuL", version = "0.1.0"))
public class TestDataGeneratorApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(TestDataGeneratorApplication.class)
                .profiles("testdatagenerator")
                .run(args);
    }
}
