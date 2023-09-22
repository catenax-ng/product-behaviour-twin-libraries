package net.catena_x.btp.hi.oem.frontend;

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
		"net.catena_x.btp.hi.oem.frontend",
		"net.catena_x.btp.hi.oem.common",
		"net.catena_x.btp.libraries.util"})
@OpenAPIDefinition(info = @Info(title = "Health indicator frontend", version = "0.1.0"))
public class OemHiFrontendApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder()
		.sources(OemHiFrontendApplication.class)
		.profiles("hifrontend")
		.run(args);
	}
}
