package net.catena_x.btp.hi.oem.backend.hi_service.controller.swagger;

public final class CollectorResetQueueDoc {
    public static final String SUMMARY = "Resets the queue.";
    public static final String DESCRIPTION = """
Resets the calculation queue. Pending calculations are not stopped, only the queue is cleared.
Waiting calculations are deleted.
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: DESCRIPTION.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-20T10:52:00.526219700Z",
  "result": "Ok",
  "message": "Queue is reset."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Some error occurred.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": ""
}
""";
}
