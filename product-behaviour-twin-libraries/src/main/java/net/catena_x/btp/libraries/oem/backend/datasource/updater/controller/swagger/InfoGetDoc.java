package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger;

public final class InfoGetDoc {
    public static final String SUMMARY = "Gets an info item.";
    public static final String DESCRIPTION = """
The info item must exist. The value is returned as value element within the default api response JSON object. 
""";

    public static final String KEY_NAME = "key";
    public static final String KEY_DESCRIPTION = """
Key of the requested info element.
""";

    public static final String KEY_EXAMPLE_1_NAME = "data version";
    public static final String KEY_EXAMPLE_1_DESCRIPTION = "Requests the data version.";
    public static final String KEY_EXAMPLE_1_VALUE = "DATAVERSION";

    public static final String KEY_EXAMPLE_2_NAME = "adaption value info";
    public static final String KEY_EXAMPLE_2_DESCRIPTION = "Requests the adaption value info.";
    public static final String KEY_EXAMPLE_2_VALUE = "ADAPTIONVALUEINFO";

    public static final String KEY_EXAMPLE_3_NAME = "load spectra info";
    public static final String KEY_EXAMPLE_3_DESCRIPTION = "Requests the load spectra info.";
    public static final String KEY_EXAMPLE_3_VALUE = "LOADSPECTRUMINFO";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Element successfully requested.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T11:40:38.060012200Z",
  "result": "Ok",
  "message": null,
  "value": "DV_0.0.99"
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Request failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T11:52:49.767321500Z",
  "result": "Error",
  "message": "Info element for key ABCDEFG not found!"
}
""";
}
