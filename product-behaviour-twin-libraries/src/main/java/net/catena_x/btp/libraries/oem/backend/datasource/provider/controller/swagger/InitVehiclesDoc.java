package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class InitVehiclesDoc {
    public static final String SUMMARY = "Initializes all vehicles from test data file.";
    public static final String DESCRIPTION = """
The vehicles must not be registered before.
Before, the reset endpoint must be called the very first the database is used or externally reset.  
Calls the data updaters vehicle register endpoint for each vehicle in the test data file.  
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: All vehicles successfully registered.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T16:21:34.338541200Z",
  "result": "Ok",
  "message": "Vehicles registered."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Vehicles not successfully registered.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:26:35.905992900Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: org.springframework.web.client.ResourceAccessException: I/O error on POST request for \\"http://localhost:25552/api/rawdata/vehicle/register\\": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect"
}
""";
}
