package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class ExportTestDataDoc {
    public static final String SUMMARY = "Exports the test data.";
    public static final String DESCRIPTION = """
The test data are exported to the configured file path. They can be split to multiple files.
""";

    public static final String LIMIT_NAME = "limitperfile";
    public static final String LIMIT_DESCRIPTION = "Limits the sets of vehicles and gearboxes per exported file.";
    public static final String LIMIT_EXAMPLE_1_NAME = "limitation to 20 vehicles";
    public static final String LIMIT_EXAMPLE_1_DESCRIPTION = "Limitation to 20 vehicles per exported file.";
    public static final String LIMIT_EXAMPLE_1_VALUE = "20";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Test data successfully exported.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T18:45:37.606237100Z",
  "result": "Ok",
  "message": "Test data exported."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Export failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:51:31.173759900Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: java.io.FileNotFoundException: Z:\\\\out\\\\testdata_export_20221219-185131.json (The system cannot find the provided path)"
}
""";
}
