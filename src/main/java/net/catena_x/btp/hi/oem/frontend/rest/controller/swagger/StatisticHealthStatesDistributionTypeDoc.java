package net.catena_x.btp.hi.oem.frontend.rest.controller.swagger;

public final class StatisticHealthStatesDistributionTypeDoc {
    public static final String SUMMARY = "Requests the distribution for a specific health indicator type.";
    public static final String DESCRIPTION = """
Requests the distribution for a specific health indicator type.
""";

    public static final String TYPE_DESCRIPTION = """
Health indicator type. Allowed values:
 * loadspectra
 * adaptionvalues
""";

    public static final String TYPE_EXAMPLE_1_NAME = "load spectra";
    public static final String TYPE_EXAMPLE_1_DESCRIPTION = "Sample asset id (ignored).";
    public static final String TYPE_EXAMPLE_1_VALUE = "loadspectra";

    public static final String TYPE_EXAMPLE_2_NAME = "adaption values";
    public static final String TYPE_EXAMPLE_2_DESCRIPTION = "Sample asset id (ignored).";
    public static final String TYPE_EXAMPLE_2_VALUE = "adaptionvalues";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Histogram successfully requested.";
    public static final String RESPONSE_OK_VALUE = """
{
  "countGreen": 3,
  "countYellow": 0,
  "countRed": 0,
  "countUnknown": 497
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Request failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-20T13:49:04.755721300Z",
  "result": "Error",
  "message": "Invalid type abc"
}
""";
}
