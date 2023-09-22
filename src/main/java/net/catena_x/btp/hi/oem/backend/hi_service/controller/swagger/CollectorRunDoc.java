package net.catena_x.btp.hi.oem.backend.hi_service.controller.swagger;

public final class CollectorRunDoc {
    public static final String SUMMARY = "Starts the external calculation service.";
    public static final String DESCRIPTION = """
Collects new telematics data and sends the input data to the external calculation service. Doesn't wait for the results.
The results are received by the notifyresult endpoint.
If there are no new telematics data, the result is ok and no calculation is started.
""";

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
