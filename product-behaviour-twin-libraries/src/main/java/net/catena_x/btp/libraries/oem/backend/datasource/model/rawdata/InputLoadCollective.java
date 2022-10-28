package net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata;

public record InputLoadCollective (
    String vehicleid,
    java.time.Instant productionDate,
    java.time.Instant timestamp,
    float mileage,
    long operatingseconds,
    String[] loadcollective
) {}
