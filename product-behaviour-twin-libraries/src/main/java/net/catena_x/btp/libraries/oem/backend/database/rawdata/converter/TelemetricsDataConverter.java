package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsDataConverterBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelemetricsData;
import org.springframework.stereotype.Component;

@Component
public final class TelemetricsDataConverter extends TelemetricsDataConverterBase<InputTelemetricsData, TelemetricsData> {
    @Override
    protected void convertSpecificData(InputTelemetricsData source, TelemetricsData destination) {
        destination.setLoadCollectives(source.loadCollectives());
        destination.setAdaptionValues(source.adaptionValues());
    }
}
