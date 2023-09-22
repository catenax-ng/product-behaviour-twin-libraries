package net.catena_x.btp.hi.oem.backend.hi_service.controller.swagger;

public final class CollectorRunTestDoc {
    public static final String SUMMARY = "Starts the external calculation service with options.";
    public static final String DESCRIPTION = """
Collects new telematics data and sends the input data to the external calculation service. Doesn't wait for the results.
The results are received by the notifyresult endpoint.
If there are no new telematics data, the result is ok and no calculation is started.
""";

    public static final String OPTIONS_DESCRIPTION = """
Options for the calculation preparation. There are four basic options:
 * R: Rename load spectra to load collectives (to be compatible to old bamm models).
 * A: Recalculate all telematics data (incl. already calculated).
 * D: Reset HI database.
 * Q: Reset calculation queue.
 * P: Use predefined alternating results for the mockup service instead of calculated ones.
 * K: Use knowledge agent for calculation (instead of direct service calls).

And the limit option at the end: Limit the count of components that are calculated.
""";

    public static final String OPTIONS_EXAMPLE_1_NAME = "Limit components";
    public static final String OPTIONS_EXAMPLE_1_DESCRIPTION = "Limit to 3 components.";
    public static final String OPTIONS_EXAMPLE_1_VALUE = "3";

    public static final String OPTIONS_EXAMPLE_2_NAME = "Reinitialize";
    public static final String OPTIONS_EXAMPLE_2_DESCRIPTION = "Reinitialize components and reset queue.";
    public static final String OPTIONS_EXAMPLE_2_VALUE = "ADQ";

    public static final String OPTIONS_EXAMPLE_3_NAME = "Rename, reinitialize and limit";
    public static final String OPTIONS_EXAMPLE_3_DESCRIPTION = "";
    public static final String OPTIONS_EXAMPLE_3_VALUE = "RAQ15";

    public static final String OPTIONS_EXAMPLE_4_NAME = "Use knowledge agent";
    public static final String OPTIONS_EXAMPLE_4_DESCRIPTION = "";
    public static final String OPTIONS_EXAMPLE_4_VALUE = "AK";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Calculation started or nothing to calculate.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-20T08:02:14.984972Z",
  "result": "Ok",
  "message": "Started external hi calculation."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Starting calculation failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-20T08:23:59.990599Z",
  "result": "Error",
  "message": "net.catena_x.btp.hi.oem.util.exceptions.OemHIException: org.springframework.web.client.HttpClientErrorException$NotFound: 404 : \\"{\\"timestamp\\":1671524639960,\\"status\\":404,\\"error\\":\\"Not Found\\",\\"path\\":\\"/api/service/urn:example:id/submodel\\"}\\""
}
""";
}