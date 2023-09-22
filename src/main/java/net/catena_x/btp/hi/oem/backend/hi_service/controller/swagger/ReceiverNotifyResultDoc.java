package net.catena_x.btp.hi.oem.backend.hi_service.controller.swagger;

public final class ReceiverNotifyResultDoc {
    public static final String SUMMARY = "Receives calculation results.";
    public static final String DESCRIPTION = """
The calculation results are calculated by an external service. This service is called by the run endpoint.
The results are mapped to the related calculation. 
""";
    public static final String BODY_DESCRIPTION = "Notification with results.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample input";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Input for three components.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "header": {
    "referencedNotificationID": "0460ab04-b482-461f-a736-f2773dd401ab",
    "senderBPN": "BPN0000SUPPLIER",
    "senderAddress": "http://edc.supplier.com/endpoint",
    "recipientAddress": "https://oem.com/edc",
    "recipientBPN": "BPN0000OEM",
    "severity": "MINOR",
    "status": "SENT",
    "targetDate": "2022-11-24T22:07:02.611048800Z",
    "timeStamp": "2022-11-24T11:24:36.744320Z",
    "classification": "HealthIndicatorResult"
  },
  "content": {
    "requestRefId": "0460ab04-b482-461f-a736-f2773dd401ab",
    "healthIndicatorOutputs": [
      {
        "version": "DV_0.0.99",
        "componentId": "urn:uuid:4773625a-5e56-4879-abed-475be29bd66b",
        "healthIndicatorValues": [
          0.0,
          0.99
        ]
      },
      {
        "version": "DV_0.0.99",
        "componentId": "urn:uuid:aaa7a395-5495-49a3-8ad7-3a66f25d388b",
        "healthIndicatorValues": [
          0.3,
          2.5
        ]
      },
      {
        "version": "DV_0.0.99",
        "componentId": "urn:uuid:13673040-413b-44e1-b1bd-ab09125da51b",
        "healthIndicatorValues": [
          1.0,
          0.6
        ]
      }
    ]
  }
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The results were mapped successfully.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-20T12:21:56.523230300Z",
  "result": "Ok",
  "message": "Processing results started."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Mapping results failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": ""
}
""";
}
