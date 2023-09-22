package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class ResetInternalTestDataDoc {
    public static final String SUMMARY = "Resets the providers internal test data.";
    public static final String DESCRIPTION = """
After the reset, the test data are new initialized when used next time.
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Test data reinitialized.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T16:20:20.665229Z",
  "result": "Ok",
  "message": "Provider internal testdata reset."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Test data could not be reinitialized.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:13:37.657502500Z",
  "result": "Error",
  "message": ""
}
""";
}
