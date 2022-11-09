package net.catena_x.btp.libraries.oem.backend.datasource.updater;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.datasource.updater",
		                       "net.catena_x.btp.libraries.oem.backend.database.rawdata"})
@EntityScan(basePackages = {"net.catena_x.btp.libraries.oem.backend.database.rawdata.model"})
@OpenAPIDefinition(info = @Info(title = "Data updater service", version = "0.0.99"))
public class OemDataUpdaterApplication {
	public static void main(@NotNull final String[] args) {
		new SpringApplicationBuilder()
		.sources(OemDataUpdaterApplication.class)
		.profiles("dataupdater")
		.run(args);
	}
}
