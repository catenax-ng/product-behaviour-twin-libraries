package net.catena_x.btp.libraries.oem.backend.datasource.provider;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.jetbrains.annotations.NotNull;
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
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.provider",
		"net.catena_x.btp.libraries.oem.backend.datasource.util",
		"net.catena_x.btp.libraries.oem.backend.datasource.model"})
@OpenAPIDefinition(info = @Info(title = "Data provider service", version = "0.0.99"))
public class OemDataProviderApplication {
	public static void main(@NotNull final String[] args) {
		new SpringApplicationBuilder()
		.sources(OemDataProviderApplication.class)
		.profiles("dataprovider")
		.run(args);
	}
}
