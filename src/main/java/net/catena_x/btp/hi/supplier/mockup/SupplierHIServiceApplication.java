package net.catena_x.btp.hi.supplier.mockup;

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
        "net.catena_x.btp.hi.supplier",
        "net.catena_x.btp.libraries.notification",
        "net.catena_x.btp.libraries.util",
        "net.catena_x.btp.libraries.util.security"})
@OpenAPIDefinition(info = @Info(title = "Supplier hi service", version = "0.1.0"))
public class SupplierHIServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SupplierHIServiceApplication.class)
                .profiles("hisupplierservice")
                .run(args);
    }
}
