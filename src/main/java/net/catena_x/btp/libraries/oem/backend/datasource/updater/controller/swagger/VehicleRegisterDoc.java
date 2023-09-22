package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger;

public final class VehicleRegisterDoc {
    public static final String SUMMARY = "Registers a new vehicle.";
    public static final String DESCRIPTION = """
The vehicle must not exist in the database yet. Vehicles have to be registered before telematics data can be uploaded for them. 
""";
    public static final String BODY_DESCRIPTION = "Vehicle to register.";

    public static final String BODY_EXAMPLE_1_NAME = "Test vehicle";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "A artificial vehicle.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "vehicleId": "urn:uuid:fb4f48c7-bc13-4148-ad22-d37a8fa63289",
  "van": "FGPTXINYZAVJY9",
  "gearboxId": "urn:uuid:13673040-413a-44e1-b1bd-ab09125da515",
  "productionDate": "2019-08-24T23:21:39Z"
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Vehicle successfully registered.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T15:02:20.987525800Z",
  "result": "Ok",
  "message": "Vehicle registered."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Registering vehicle failed. " +
            "If the vehicle was already registered, the registration fails.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T15:03:16.105446500Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException: Failed to register vehicle!"
}
""";
}
