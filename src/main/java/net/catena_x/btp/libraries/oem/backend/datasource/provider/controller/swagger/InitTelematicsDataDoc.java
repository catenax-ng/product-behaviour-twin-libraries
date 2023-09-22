package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class InitTelematicsDataDoc {
    public static final String SUMMARY = "Initializes all telemetics data from test data file.";
    public static final String DESCRIPTION = """
The vehicles must be registered before.
If the call fails, make sure that the database is initialized properly. The reset endpoint must be called the very first
the database is used or externally reset. If not, vehicle registration is possible, but the telematics data initializing
will fail because the sync counter is not initialized.
Calls the data updaters telematics endpoint for each data set in the test data file.  
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Telematics data successfully initialized.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T16:23:28.737907300Z",
  "result": "Ok",
  "message": "Telematics data initialized."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Initializing telematics data failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:32:19.749964800Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.UncheckedDataProviderException: org.springframework.web.client.ResourceAccessException: I/O error on POST request for \\"http://localhost:25552/api/rawdata/telematicsdata/add\\": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect"
}
""";
}
