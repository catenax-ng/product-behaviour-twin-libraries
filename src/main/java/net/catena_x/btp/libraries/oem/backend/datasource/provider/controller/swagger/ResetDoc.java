package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class ResetDoc {
    public static final String SUMMARY = "Calls the data updaters reset endpoint.";
    public static final String DESCRIPTION = """
Reinitializes all info elements and clears vehicles and telematics data.
This endpoint must be called after an external database reset or when connecting to a new, empty database.
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Database reinitialized.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T16:20:20.665229Z",
  "result": "Ok",
  "message": "Database reinitialized."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Database could not be reinitialized.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:13:37.657502500Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: org.springframework.web.client.ResourceAccessException: I/O error on GET request for \\"http://localhost:25552/api/rawdata/reset\\": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect"
}
""";
}
