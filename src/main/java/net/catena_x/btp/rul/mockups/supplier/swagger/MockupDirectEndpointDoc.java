package net.catena_x.btp.rul.mockups.supplier.swagger;

public final class MockupDirectEndpointDoc {
    public static final String SUMMARY = "MockUp endpoint to dispatch mockup requesters over simulated api wrapper edc.";
    public static final String DESCRIPTION = """
This endpoint is for local tests.
""";

    public static final String BODY_DESCRIPTION = "RuL calculation result notification.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample input for Knowledge Agent call ";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Sample input data.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "header": {
    "notificationID": "98f507d5-175d-4945-8d06-6aa1fcef9a0c",
    "senderBPN": "BPN0000SUPPLIER",
    "senderAddress": "edcs://supplier.com/edc",
    "recipientAddress": "https://supplier.com/edc",
    "recipientBPN": "BPN0000SUPPLIER",
    "severity": "MINOR",
    "status": "SENT",
    "targetDate": "2022-11-24T22:07:02.611048800Z",
    "timeStamp": "2022-11-24T11:24:36.744320Z",
    "classification": "RemainingUsefulLifePredictor",
    "respondAssetId": "http://knowledgeagent/response"
  },
  "content": {
    "requestRefId": "98f507d5-175d-4945-8d06-6aa1fcef9a0c",
    "endurancePredictorInputs": [
      {
        "componentId": "urn:uuid:b43a1fab-f460-4d24-b078-e58a0247ad1e",
        "classifiedLoadSpectrumGearOil": {
          "targetComponentID": "urn:uuid:b43a1fab-f460-4d24-b078-e58a0247ad1e",
          "metadata": {
            "projectDescription": "pnr_76543",
            "componentDescription": "GearOil",
            "routeDescription": "logged",
            "status": {
              "date": "2023-02-19T10:42:14.213+01:00",
              "operatingHours": 32137.9,
              "mileage": 865432
            }
          },
          "header": {
            "countingValue": "Time",
            "countingUnit": "unit:secondUnitOfTime",
            "countingMethod": "TimeAtLevel",
            "channels": [
              {
                "channelName": "TC_SU",
                "unit": "unit:degreeCelsius",
                "lowerLimit": 0.0,
                "upperLimit": 640.0,
                "numberOfBins": 128
              }
            ]
          },
          "body": {
            "classes": [
              {
                "className": "TC_SU-class",
                "classList": [
                  14,
                  15,
                  16,
                  17,
                  18,
                  19,
                  20,
                  21,
                  22
                ]
              }
            ],
            "counts": {
              "countsName": "Time",
              "countsList": [
                34968.93,
                739782.51,
                4013185.15,
                46755055.56,
                25268958.35,
                8649735.95,
                9383635.35,
                19189260.77,
                1353867.54
              ]
            }
          },
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
        }
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
  "message": "Accepted."
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
