package net.catena_x.btp.rul.mockups.dispatcher.swagger;

public final class MockupDispatcherDoc {
    public static final String SUMMARY = "MockUp endpoint to dispatch mockup requesters over simulated api wrapper edc.";
    public static final String DESCRIPTION = """
This endpoint is for local tests.
""";

    public static final String ASSETID_DESCRIPTION = """
Asset id. This id is used to determine the class to call.
""";

    public static final String ASSETID_EXAMPLE_1_NAME = "Asset id";
    public static final String ASSETID_EXAMPLE_1_DESCRIPTION = "Asset id.";
    public static final String ASSETID_EXAMPLE_1_VALUE = "urn:uuid:17937c84-000b-4520-9830-ba86934850e3";

    public static final String PROVIDERCONNECTORURL_DESCRIPTION = """
Url of the provider connector (not used in the mock up).
""";

    public static final String PROVIDERCONNECTORURL_EXAMPLE_1_NAME = "Sample address";
    public static final String PROVIDERCONNECTORURL_EXAMPLE_1_DESCRIPTION = "Sample provider connector url (ignored).";
    public static final String PROVIDERCONNECTORURL_EXAMPLE_1_VALUE = "http://edc.oem.com/endpoint";

    public static final String BODY_DESCRIPTION = "RuL calculation result notification.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample input data";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Sample input data, received after a simulated calculation.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "header": {
    "notificationID": "98f507d5-175d-4945-8d06-6aa1fcef9a0c",
    "senderBPN": "BPN0000OEM",
    "senderAddress": "https://connector2.cp.int.adac.openresearch.com",
    "recipientAddress": "https://supplier.com/edc",
    "recipientBPN": "BPN0000SUPPLIER",
    "severity": "MINOR",
    "status": "SENT",
    "targetDate": "2022-11-24T22:07:02.611048800Z",
    "timeStamp": "2022-11-24T11:24:36.744320Z",
    "classification": "RemainingUsefulLifePredictor",
    "respondAssetId": "test"
  },
  "content": {
    "requestRefId": "98f507d5-175d-4945-8d06-6aa1fcef9a0c",
    "endurancePredictorInputs": [
      {
        "componentId": "urn:uuid:b43a1fab-f460-4d24-b078-e58a0247ad1e",
        "classifiedLoadSpectrumGearSet": {
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum",
          "targetComponentID": "urn:uuid:1d161134-8bd4-4253-8735-304852d1d17b",
          "metadata": {
              "projectDescription": "projectnumber Stadt",
              "componentDescription": "GearSet",
              "routeDescription": "logged",
              "status": {
                "date": "2022-08-11T09:22:11.999+01:00",
                "operatingHours": 3213.9,
                "mileage": 65432
              }
            },
              "header": {
              "countingMethod": "LRD",
              "channels": [
                  {
                      "channelName": "N_TU",
                      "unit": "unit:rpm",
                      "lowerLimit": -100.0,
                      "upperLimit": 12700.0,
                      "numberOfBins": 128
                  },
                  {
                      "channelName": "T_TU",
                      "unit": "unit:Nm",
                      "lowerLimit": -1270.0,
                      "upperLimit": 1290.0,
                      "numberOfBins": 128
                  },
                  {
                      "channelName": "Z_GANG",
                      "unit": "unit:ONE",
                      "lowerLimit": -0.5,
                      "upperLimit": 9.5,
                      "numberOfBins": 10
                  }
              ],
              "countingUnit": "unit:ONE"
          },
          "body": { "classes": [\s
              { "className": "N_TU-class", "classList": [1]},
              {"className": "T_TU-class", "classList": [68]},
              {"className": "Z_GANG-class", "classList": [1]}], "counts":{
              "countsName":"Counts", "countsList": [0.42145895]}
          }
      },
        "classifiedLoadSpectrumGearOil": {
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum",
          "targetComponentID": "urn:uuid:1d161134-8bd4-4253-8735-304852d1d17b",
          "metadata": {
              "projectDescription": "projectnumber Stadt",
              "componentDescription": "GearOil",
              "routeDescription": "logged",
              "status": {
                "date": "2022-08-11T10:42:14.213+01:00",
                "operatingHours": 3213.9,
                "mileage": 65432
              }
            },
            "header": {
              "countingMethod": "TimeAtLevel",
              "channels": [
                  {
                      "channelName": "TC_SU",
                      "unit": "unit:degreeCelsius",
                      "lowerLimit": 0.0,
                      "upperLimit": 640.0,
                      "numberOfBins": 128
                  }
              ],
              "countingValue": "Time",
              "countingUnit": "unit:secondUnitOfTime"
          },
          "body": { "classes": [
              {"className": "TC_SU-class", "classList": [14]}], "counts":{
              "countsName":"Time", "countsList": [34968.93]}
          }
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
