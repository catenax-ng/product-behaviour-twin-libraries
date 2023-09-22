package net.catena_x.btp.hi.oem.frontend.rest.controller.swagger;

public final class VehicleGearboxIdDoc {
    public static final String SUMMARY = "Requests a vehicle by its gearbox id.";
    public static final String DESCRIPTION = """
Requests a vehicle by its gearbox id.
""";

    public static final String GEARBOXID_DESCRIPTION = """
Gearbox id of the requested vehicle.
""";

    public static final String GEARBOXID_EXAMPLE_1_NAME = "Sample id";
    public static final String GEARBOXID_EXAMPLE_1_DESCRIPTION = "Sample gearbox id.";
    public static final String GEARBOXID_EXAMPLE_1_VALUE = "urn:uuid:b6309b8a-20c0-4e7d-b782-a7c303bb7da4";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Requested vehicle.";
    public static final String RESPONSE_OK_VALUE = """
{
  "vehicleId": "urn:uuid:8d6e2e3f-6798-4e1d-8eae-eb4318a7d487",
  "van": "RGZCDKEJHDSNCB",
  "gearboxId": "urn:uuid:b6309b8a-20c0-4e7d-b782-a7c303bb7da4",
  "productionDate": "2014-06-26",
  "updateTimestamp": "2022-12-20",
  "healthStateLoadSpectra": "GREEN",
  "healthStateAdaptionValues": "GREEN"
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Request failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-20T14:00:41.341266900Z",
  "result": "Error",
  "message": "Vehicle not found!"
}
""";
}
