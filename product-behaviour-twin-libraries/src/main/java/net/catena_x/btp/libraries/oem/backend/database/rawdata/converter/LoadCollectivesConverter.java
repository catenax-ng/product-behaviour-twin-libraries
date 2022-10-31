package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.LoadCollectives;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputLoadCollectives;
import org.springframework.stereotype.Component;

@Component
public final class LoadCollectivesConverter extends TelemetricsDataConverter<InputLoadCollectives, LoadCollectives> {
    @Override
    protected void convertSpecificData(InputLoadCollectives source, LoadCollectives destination) {
        destination.setLoadcollectives(source.loadcollectives());
    }
}
