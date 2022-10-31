package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base;

import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;

public abstract class TelemetricsDataConverter<RawInputType extends TelemetricsRawInputSource,
        DBEntityType extends TelemetricsDBEntityDestination> {

    public void convertWithoutId(RawInputType source, DBEntityType destination) {
        convertState(destination, source.state());
        convertSpecificData(source, destination);
    }

    public void convertAndSetId(RawInputType source, DBEntityType destination, String id) {
        convertWithoutId(source, destination);
        destination.setId(id);
    }

    private void convertState(DBEntityType converted, VehicleState state) {
        converted.setVehicleid(state.vehicleid());
        converted.setTimestamp(state.timestamp());
        converted.setMileage(state.mileage());
        converted.setOperatingseconds(state.operatingseconds());
    }

    protected abstract void convertSpecificData(RawInputType source, DBEntityType destination);
}
