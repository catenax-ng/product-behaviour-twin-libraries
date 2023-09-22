package net.catena_x.btp.hi.oem.backend.hi_service.collector.util;

import net.catena_x.btp.hi.oem.backend.hi_service.collector.HIDataCollector;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.dto.infoitem.InfoTable;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class HIVehicleCollector {
    @Autowired private VehicleTable vehicleTable;
    @Autowired private InfoTable infoTable;

    private final Logger logger = LoggerFactory.getLogger(HIVehicleCollector.class);

    public List<Vehicle> collect(@NotNull final long syncCounterMin) throws OemHIException {
        checkDataVersion();

        try {
            final List<Vehicle> updatedVehicles = collectUpdatedVehicles(syncCounterMin);

            if(DataHelper.isNullOrEmpty(updatedVehicles)) {
                return null;
            }

            return updatedVehicles;
        } catch (final OemDatabaseException exception) {
            throw new OemHIException(exception);
        }
    }

    private List<Vehicle> collectUpdatedVehicles(@NotNull final long syncCounterSince) throws OemDatabaseException {
        return vehicleTable.getSyncCounterSinceWithTelematicsDataNewTransaction(syncCounterSince);
    }

    private void checkDataVersion() throws OemHIException {
        try {
            if(!infoTable.getInfoValueNewTransaction(InfoKey.DATAVERSION).equals(HIDataCollector.DATA_VERSION)) {
                throw new OemHIException("Data Version has changed!");
            }
        } catch (final OemDatabaseException exception) {
            throw new OemHIException(exception);
        }
    }
}
