package net.catena_x.btp.hi.oem.common.database.hi.tables.vehicle;

import net.catena_x.btp.hi.oem.common.database.hi.tables.healthindicators.HIHealthIndicatorsDAO;

public record HIVehicleWithHealthIndicatorsDAO(
    HIVehicleDAO vehicle,
    HIHealthIndicatorsDAO healthIndicators
) {}
