package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.config;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataTableInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SchemaAdjuster {
    @Autowired
    TelematicsDataTableInternal telematicsDataTableInternal;

    @Value("${rawdatadb.drivername}") private String dbDriverName;

    @EventListener(ContextRefreshedEvent.class)
    private void adjustSchemata() {
        final boolean isPostgreSQL = dbDriverName.toLowerCase().contains("postgresql");
        adjustTelematicsData(isPostgreSQL);
    }

    private void adjustTelematicsData(@NotNull final boolean isPostgreSQL) {
        telematicsDataTableInternal.adjust(isPostgreSQL);
    }
}
