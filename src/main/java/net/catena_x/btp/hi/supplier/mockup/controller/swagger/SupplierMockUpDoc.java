package net.catena_x.btp.hi.supplier.mockup.controller.swagger;

public class SupplierMockUpDoc {
    public static final String SUMMARY = "MockUp endpoint to simulate the external calculation service.";
    public static final String DESCRIPTION = """
This endpoint is for local tests. It is called from the run endpoint and calls the notifyresult endpoint. 
""";

    public static final String ASSETID_DESCRIPTION = """
Asset id (not used in the mock up).
""";

    public static final String ASSETID_EXAMPLE_1_NAME = "Sample id";
    public static final String ASSETID_EXAMPLE_1_DESCRIPTION = "Sample asset id (ignored).";
    public static final String ASSETID_EXAMPLE_1_VALUE = "04654678-b4aa-4aaf-ab36-6211da401ab";

    public static final String PROVIDERCONNECTORURL_DESCRIPTION = """
Url of the provider connector (not used in the mock up).
""";

    public static final String PROVIDERCONNECTORURL_EXAMPLE_1_NAME = "Sample address";
    public static final String PROVIDERCONNECTORURL_EXAMPLE_1_DESCRIPTION = "Sample provider connector url (ignored).";
    public static final String PROVIDERCONNECTORURL_EXAMPLE_1_VALUE = "http://edc.supplier.com/endpoint";

    public static final String BODY_DESCRIPTION = "Calculation input data.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample input data";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Sample input data for the simulated calculation service.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "header": {
    "notificationID": "29bf37dd-90d3-4759-a17e-7c02234b6c62",
    "senderBPN": "BPN0000OEM",
    "senderAddress": "https://oem.com/edc",
    "recipientBPN": "BPN0000SUPPLIER",
    "recipientAddress": "http://edc.supplier.com/endpoint",
    "classification": "HISERVICE",
    "severity": "MINOR",
    "status": "SENT",
    "targetDate": "2022-12-21T00:44:52.597897100Z",
    "timeStamp": "2022-12-20T12:44:52.597897100Z"
  },
  "content": {
    "requestRefId": "29bf37dd-90d3-4759-a17e-7c02234b6c62",
    "healthIndicatorInputs": [
      {
        "componentId": "urn:uuid:aaa7a395-5495-49a3-8ad7-3a66f25d388d",
        "classifiedLoadSpectrum": {
          "targetComponentID": "urn:uuid:b92d110a-00e9-49a5-9cde-5759bc277de0",
          "metadata": {
            "projectDescription": "projectnumber Landstrasse",
            "componentDescription": "Clutch",
            "routeDescription": "logged",
            "status": {
              "date": "2022-08-21T00:00:00Z",
              "operatingHours": 1281.9,
              "mileage": 76543
            }
          },
          "header": {
            "countingValue": "Time",
            "countingUnit": "unit:secondUnitOfTime",
            "countingMethod": "TimeAtLevel",
            "channels": [
              {
                "unit": "unit:degreeCelsius",
                "numberOfBins": 8,
                "channelName": "TC_Clutch",
                "upperLimit": 320.0,
                "lowerLimit": 0.0
              }
            ]
          },
          "body": {
            "counts": {
              "countsName": "Time",
              "countsList": [
                3018.21, 3451252.83, 699160.662, 349580.331, 116526.777]
            },
            "classes": [
              {
                "className": "TC_Clutch-class",
                "classList": [2, 3, 4, 5, 6]
              }
            ]
          },
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
        },
        "adaptionValueList": {
          "version": "DV_0.0.99",
          "timestamp": "2022-08-11T00:00:00Z",
          "mileage_km": 65432.0,
          "operatingtime_s": 11570040,
          "values": [0.5, 16554.6, 234.3, 323.0]
        }
      },
      {
        "componentId": "urn:uuid:13673040-413b-44e1-b1bd-ab09125da513",
        "classifiedLoadSpectrum": {
          "targetComponentID": "urn:uuid:84db4c59-5fa1-4266-9e4c-94bea3cf72a4",
          "metadata": {
            "projectDescription": "projectnumber BAB",
            "componentDescription": "Clutch",
            "routeDescription": "logged",
            "status": {
              "date": "2022-03-15T00:00:00Z",
              "operatingHours": 262.6,
              "mileage": 23456
            }
          },
          "header": {
            "countingValue": "Time",
            "countingUnit": "unit:secondUnitOfTime",
            "countingMethod": "TimeAtLevel",
            "channels": [
              {
                "unit": "unit:degreeCelsius",
                "numberOfBins": 8,
                "channelName": "TC_Clutch",
                "upperLimit": 320.0,
                "lowerLimit": 0.0
              }
            ]
          },
          "body": {
            "counts": {
              "countsName": "Time",
              "countsList": [769.6, 15222.4, 631520.8, 304929.6, 196.8]
            },
            "classes": [
              {
                "className": "TC_Clutch-class",
                "classList": [2, 3, 4, 5, 6]
              }
            ]
          },
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
        },
        "adaptionValueList": {
          "version": "DV_0.0.99",
          "timestamp": "2022-08-11T00:00:00Z",
          "mileage_km": 65432.0,
          "operatingtime_s": 11570040,
          "values": [0.5, 16554.6, 234.3, 323.0]
        }
      },
      {
        "componentId": "urn:uuid:b6309b8a-20c0-4e7d-b782-a7c303bb7da4",
        "classifiedLoadSpectrum": {
          "targetComponentID": "urn:uuid:cd0f9c56-5687-4c10-ac36-56693caa5366",
          "metadata": {
            "projectDescription": "projectnumber Stadt",
            "componentDescription": "Clutch",
            "routeDescription": "logged",
            "status": {
              "date": "2022-08-11T00:00:00Z",
              "operatingHours": 3213.9,
              "mileage": 65432
            }
          },
          "header": {
            "countingValue": "Time",
            "countingUnit": "unit:secondUnitOfTime",
            "countingMethod": "TimeAtLevel",
            "channels": [
              {
                "unit": "unit:degreeCelsius",
                "numberOfBins": 8,
                "channelName": "TC_Clutch",
                "upperLimit": 320.0,
                "lowerLimit": 0.0
              }
            ]
          },
          "body": {
            "counts": {
              "countsName": "Time",
              "countsList": [769.6, 15222.4, 631520.8, 304929.6, 196.8]
            },
            "classes": [
              {
                "className": "TC_Clutch-class",
                "classList": [2, 3, 4, 5, 6]
              }
            ]
          },
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
        },
        "adaptionValueList": {
          "version": "DV_0.0.99",
          "timestamp": "2022-08-11T00:00:00Z",
          "mileage_km": 65432.0,
          "operatingtime_s": 11570040,
          "values": [0.5, 16554.6, 234.3, 323.0]
        }
      }
    ]
  }
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The calculation was started successfully.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-20T12:21:56.523230300Z",
  "result": "Ok",
  "message": "Accepted."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Starting calculation failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": ""
}
""";
}
