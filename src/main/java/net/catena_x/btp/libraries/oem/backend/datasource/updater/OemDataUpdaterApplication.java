package net.catena_x.btp.libraries.oem.backend.datasource.updater;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import javax.validation.constraints.NotNull;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
		"net.catena_x.btp.libraries.oem.backend.datasource.util",
		"net.catena_x.btp.libraries.oem.backend.model",
		"net.catena_x.btp.libraries.oem.backend.database.rawdata",
		"net.catena_x.btp.libraries.util",
		"net.catena_x.btp.libraries.util.security"})
@OpenAPIDefinition(info = @Info(title = "Data updater service", version = "0.1.0"))
public class OemDataUpdaterApplication {
	public static void main(@NotNull final String[] args) {
		new SpringApplicationBuilder()
		.sources(OemDataUpdaterApplication.class)
		.profiles("dataupdater")
		.run(args);
	}
}