package net.catena_x.btp.sedc.apps.oem.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import javax.validation.constraints.NotNull;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {
        "net.catena_x.btp.sedc.apps.oem.collector",
        "net.catena_x.btp.libraries.util",
        "net.catena_x.btp.libraries.edc",
        "net.catena_x.btp.sedc.model",
        "net.catena_x.btp.sedc.protocol"})
@OpenAPIDefinition(info = @Info(title = "SEDC data collector service", version = "0.1.0"))
public class OemDataCollectorApplication {
    public static void main(@NotNull final String[] args) {
        new SpringApplicationBuilder()
                .sources(OemDataCollectorApplication.class)
                .profiles("oemdatacollector")
                .run(args);
    }
}
