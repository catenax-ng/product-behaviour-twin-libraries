package net.catena_x.btp.rul.oem.backend.util.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RuLStarterCalculationType {
    @JsonProperty("OkSupplierCalculationServiceStarted")
    OK_SUPPLIER_CALCULATION_SERVICE_STARTED,

    @JsonProperty("SupplierCalculationServiceFailed")
    SUPPLIER_CALCULATION_SERVICE_FAILED,

    @JsonProperty("NoVinGiven")
    NO_VIN_GIVEN,

    @JsonProperty("NoDataForVehicle")
    NO_DATA_FOR_VEHICLE,

    @JsonProperty("UnknownVehicle")
    UNKNOWN_VEHICLE,

    @JsonProperty("RequiredLoadSpectrumTypeNotFound")
    REQUIRED_LOAD_SPECTRUM_TYPE_NOT_FOUND,

    @JsonProperty("RequestError")
    REQUEST_ERROR,

    @JsonProperty("InternalError")
    INTERNAL_ERROR
}
