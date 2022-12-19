package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger;

public final class ResetDoc {
    public static final String SUMMARY = "Resets all info elements.";
    public static final String DESCRIPTION = """
The info elements are all deleted and reinitialized. They do not have to be initialized with the init endpoint.
""";

    public static final String RESPONSE_OK_DESCRIPTION = "TODO: DESCRIPTION.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T14:47:26.108411600Z",
  "result": "Ok",
  "message": "Rawdata database cleared and reinitialized."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "TODO: DESCRIPTION.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": ""
}
""";
}
