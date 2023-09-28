package net.catena_x.btp.sedc.apps.supplier.calculation;

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
        "net.catena_x.btp.sedc.apps.supplier.calculation",
        "net.catena_x.btp.libraries.util",
        "net.catena_x.btp.libraries.edc",
        "net.catena_x.btp.sedc.model",
        "net.catena_x.btp.sedc.protocol"})
@OpenAPIDefinition(info = @Info(title = "SEDC peak-load calculation service", version = "0.1.0"))
public class SupplierPeakLoadServiceApplication {
    public static void main(@NotNull final String[] args) {
        new SpringApplicationBuilder()
                .sources(SupplierPeakLoadServiceApplication.class)
                .profiles("supplierpeakloadservice")
                .run(args);
    }
}
