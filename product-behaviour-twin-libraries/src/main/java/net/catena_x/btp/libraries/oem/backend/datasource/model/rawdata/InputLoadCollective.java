package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

// TODO common base class?

public record InputLoadCollective (
    String vehicleid,
    java.time.Instant timestamp,
    float mileage,
    long operatingseconds,
    String loadcollective
) {}
