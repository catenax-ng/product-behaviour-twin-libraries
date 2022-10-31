package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.AdaptionValues;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputAdaptionValues;
import org.springframework.stereotype.Component;

@Component
public final class AdaptionValuesConverter extends TelemetricsDataConverter<InputAdaptionValues, AdaptionValues> {
    @Override
    protected void convertSpecificData(InputAdaptionValues source, AdaptionValues destination) {
        destination.setAdaptionvalues(source.adaptionvalues());
    }
}
