package net.catena_x.btp.libraries.oem.backend.datasource.updater;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan
@OpenAPIDefinition(info = @Info(title = "Data updater service", version = "0.0.99"))
public class OemDataUpdaterApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder()
		.sources(OemDataUpdaterApplication.class)
		.profiles("dataupdater")
		.run(args);
	}
}
