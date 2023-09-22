package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;

import java.util.List;

public record InputTelematicsData(
        String vehicleId,
        List<ClassifiedLoadSpectrum> loadSpectra,
        List<AdaptionValues> adaptionValues
){}
