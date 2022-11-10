package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.vehicle;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata.TelematicsDataDAO;

public record VehicleWithTelematicsDataDAO(
        VehicleDAO vehicle,
        TelematicsDataDAO telematicsData
) {}
