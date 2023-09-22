package net.catena_x.btp.rul.oem.backend.rul_service;

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
		"net.catena_x.btp.rul.oem.backend",
		"net.catena_x.btp.rul.oem.util",
		"net.catena_x.btp.rul.mockups",
		"net.catena_x.btp.libraries.notification",
		"net.catena_x.btp.libraries.oem.backend.model.dto",
///     "net.catena_x.btp.libraries.oem.backend.cloud",
		"net.catena_x.btp.libraries.oem.backend.edc",
		"net.catena_x.btp.libraries.util",
		"net.catena_x.btp.libraries.edc",
		"net.catena_x.btp.libraries.oem.backend.database",
		"net.catena_x.btp.libraries.util.security"})
@OpenAPIDefinition(info = @Info(title = "OEM rul backend service", version = "0.1.0"))
public class OemRulBackendServiceApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(OemRulBackendServiceApplication.class)
				.profiles("rulbackendservice")
				.run(args);
	}
}
