package net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger;

public final class ReceiverNotifyResultDoc {
    public static final String SUMMARY = "Receives calculation results and forwards it to requester.";
    public static final String DESCRIPTION = """
The calculation results are calculated by an external service. This service is called by the notifycalculation endpoint.
There it one result per calculation. The result is mapped to the related calculation and forwarded to the  requester. 
""";
    public static final String BODY_DESCRIPTION = "Notification with result.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample result";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Result for a vehicle.";
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
    "classification": "RemainingUsefulLifeResult"
  },
  "content": {
    "requestRefId": "0460ab04-b482-461f-a736-f2773dd401ab",
    "rulOutputs": [
      {
        "componentId": "urn:uuid:4773625a-5e56-4879-abed-475be29bd66b",
        "remainingUsefulLife": {
          "remainingOperatingHours": 4566,
          "remainingRunningDistance": 1234,
          "determinationLoaddataSource": {
            "informationOriginLoadSpectrum": "loggedOEM"
          },
          "determinationStatus": {
            "date": "2022-12-20T12:21:55.523230300Z",
            "operatingHours": 4355,
            "mileage": 12345 
          }
        }  
      }
    ]
  }
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The result was mapped successfully.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-20T12:21:56.523230300Z",
  "result": "Ok",
  "message": "Forwarded RuL calculation result with id f450ab04-b482-461f-a736-f2773dd45767 successfully."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Mapping result failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2023-01-12T21:19:24.629106100Z",
  "result": "Error",
  "message": "Failed to process RuL calculation with id 0460ab04-b482-461f-a736-f2dd406781ab: Unknown calculation id 0460ab04-b482-461f-a736-f2dd406781ab!"
}
""";
}
