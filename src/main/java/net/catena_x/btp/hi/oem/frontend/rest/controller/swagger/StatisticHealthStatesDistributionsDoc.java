package net.catena_x.btp.hi.oem.frontend.rest.controller.swagger;

public final class StatisticHealthStatesDistributionsDoc {
    public static final String SUMMARY = "Requests the distribution for all health indicator types.";
    public static final String DESCRIPTION = """
Requests the distribution for all health indicator types.
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Health states successfully requested.";
    public static final String RESPONSE_OK_VALUE = """
{
  "histogramLoadSpectra": {
    "countGreen": 2,
    "countYellow": 0,
    "countRed": 1,
    "countUnknown": 497
  },
  "histogramAdaptionValues": {
    "countGreen": 3,
    "countYellow": 0,
    "countRed": 0,
    "countUnknown": 497
  }
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Request failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": ""
}
""";
}
