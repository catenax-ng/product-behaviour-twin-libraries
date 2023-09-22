package net.catena_x.btp.hi.oem.frontend.rest.controller.swagger;

public final class VehicleVanDoc {
    public static final String SUMMARY = "Requests a vehicle by its VAN.";
    public static final String DESCRIPTION = """
Requests a vehicle by its VAN.
""";

    public static final String VAN_DESCRIPTION = """
VAN of the requested vehicle.
""";

    public static final String VAN_EXAMPLE_1_NAME = "Sample VAN";
    public static final String VAN_EXAMPLE_1_DESCRIPTION = "Sample VAN.";
    public static final String VAN_EXAMPLE_1_VALUE = "DVAJDTLJMKKZGY";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Requested vehicle.";
    public static final String RESPONSE_OK_VALUE = """
{
  "vehicleId": "urn:uuid:0323a9cb-8dad-4b01-9b0c-6b715b96ff10",
  "van": "DVAJDTLJMKKZGY",
  "gearboxId": "urn:uuid:13673040-413b-44e1-b1bd-ab09125da513",
  "productionDate": "2017-03-19",
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
