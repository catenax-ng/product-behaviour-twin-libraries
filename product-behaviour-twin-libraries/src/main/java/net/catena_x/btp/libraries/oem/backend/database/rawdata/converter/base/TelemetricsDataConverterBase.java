package net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base;

import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.VehicleState;

public abstract class TelemetricsDataConverterBase<RawInputType extends TelemetricsRawInputSource,
        DBEntityType extends TelemetricsDBEntityDestination> {

    /* This methode has to implement all conversions, except the common state item. */
    protected abstract void convertSpecificData(RawInputType source, DBEntityType destination);

    /* Factory to generate new destination Objects. */
    protected abstract DBEntityType makeNew();

    public DBEntityType convertWithoutId(RawInputType source) {
        DBEntityType destination = makeNew();
        convertState(destination, source.state());
        convertSpecificData(source, destination);
        return destination;
    }

    public DBEntityType convertAndSetId(RawInputType source, String id) {
        DBEntityType destination = convertWithoutId(source);
        destination.setId(id);
        return destination;
    }

    private void convertState(DBEntityType converted, VehicleState state) {
        converted.setVehicleId(state.vehicleId());
        converted.setCreationTimestamp(state.creationTimestamp());
        converted.setMileage(state.mileage());
        converted.setOperatingSeconds(state.operatingSeconds());
    }
}
