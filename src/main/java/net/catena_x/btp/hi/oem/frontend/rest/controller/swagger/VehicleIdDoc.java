package net.catena_x.btp.hi.oem.frontend.rest.controller.swagger;

public final class VehicleIdDoc {
    public static final String SUMMARY = "Requests a vehicle by its id.";
    public static final String DESCRIPTION = """
Requests a vehicle by its id. 
""";

    public static final String VEHICLEID_DESCRIPTION = """
Id of the requested vehicle.
""";

    public static final String VEHICLEID_EXAMPLE_1_NAME = "Sample id";
    public static final String VEHICLEID_EXAMPLE_1_DESCRIPTION = "Sample vehicle id.";
    public static final String VEHICLEID_EXAMPLE_1_VALUE = "urn:uuid:07cb70c1-944d-4731-bc14-ff81ee37370f";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Requested vehicle.";
    public static final String RESPONSE_OK_VALUE = """
{
  "vehicleId": "urn:uuid:07cb70c1-944d-4731-bc14-ff81ee37370f",
  "van": "DSVTKJIQTPCJOA",
  "gearboxId": "urn:uuid:91f428dd-3355-4c6a-96f1-3343b3f3a097",
  "productionDate": "2013-08-03",
  "updateTimestamp": "2022-12-20",
  "healthStateLoadSpectra": "CALCULATION_PENDING",
  "healthStateAdaptionValues": "CALCULATION_PENDING"
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
