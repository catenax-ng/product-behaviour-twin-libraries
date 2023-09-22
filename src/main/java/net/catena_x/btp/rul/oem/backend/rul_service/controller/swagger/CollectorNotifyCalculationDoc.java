package net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger;

public final class CollectorNotifyCalculationDoc {
    public static final String SUMMARY = "Starts a RuL calculation.";
    public static final String DESCRIPTION = """
The calculation results are calculated by an external service. The collector translates the given vin, collects the
related load spectra and starts the extern calculation by sending a calculation notification to the supplier. 
""";
    public static final String BODY_DESCRIPTION = "Notification with the vin of the related vehicle.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample request";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Request for the RuL calculation of a vehicle gearbox.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "header": {
    "notificationID": "0460ab04-b482-461f-a736-f2773dd401ab",
    "senderBPN": "BPN0000SUPPLIER",
    "senderAddress": "http://edc.supplier.com/endpoint",
    "recipientAddress": "https://oem.com/edc",
    "recipientBPN": "BPN0000OEM",
    "severity": "MINOR",
    "status": "SENT",
    "targetDate": "2022-11-24T22:07:02.611048800Z",
    "timeStamp": "2022-11-24T11:24:36.744320Z",
    "classification": "RemainingUsefulLifePredictor"
  },
  "content": {
    "vin": "WBAVP11070VR72002"
  }
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The request was forwarded successfully.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2023-01-12T20:19:55.647247100Z",
  "type": "OkCalculating",
  "message": "Started RuL calculation successfully."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: The request failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": "Starting RuL calculation failed: Unknown VIN!"
}
""";
}

