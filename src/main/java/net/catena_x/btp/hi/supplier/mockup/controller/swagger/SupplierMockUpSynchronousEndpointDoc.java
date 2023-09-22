package net.catena_x.btp.hi.supplier.mockup.controller.swagger;

public class SupplierMockUpSynchronousEndpointDoc {
    public static final String SUMMARY = "MockUp endpoint to simulate the external calculation service with synchronous result.";
    public static final String DESCRIPTION = """
This endpoint is for local tests. It is called by the knowledge agent and fires no notifications. 
""";

    public static final String BODY_DESCRIPTION = SupplierMockUpDoc.BODY_DESCRIPTION;

    public static final String BODY_EXAMPLE_1_NAME = SupplierMockUpDoc.BODY_EXAMPLE_1_NAME;
    public static final String BODY_EXAMPLE_1_DESCRIPTION = SupplierMockUpDoc.BODY_EXAMPLE_1_DESCRIPTION;
    public static final String BODY_EXAMPLE_1_VALUE = """
{
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
              "upperLimit": 320,
              "lowerLimit": 0
            }
          ]
        },
        "body": {
          "counts": {
            "countsName": "Time",
            "countsList": [
              3018.21,
              3451252.83,
              699160.662,
              349580.331,
              116526.777
            ]
          },
          "classes": [
            {
              "className": "TC_Clutch-class",
              "classList": [
                2,
                3,
                4,
                5,
                6
              ]
            }
          ]
        },
        "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
      },
      "adaptionValueList": {
        "version": "DV_0.0.99",
        "timestamp": "2022-08-11T00:00:00Z",
        "mileage_km": 65432,
        "operatingtime_s": 11570040,
        "values": [
          0.5,
          16554.6,
          234.3,
          323
        ]
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
              "upperLimit": 320,
              "lowerLimit": 0
            }
          ]
        },
        "body": {
          "counts": {
            "countsName": "Time",
            "countsList": [
              769.6,
              15222.4,
              631520.8,
              304929.6,
              196.8
            ]
          },
          "classes": [
            {
              "className": "TC_Clutch-class",
              "classList": [
                2,
                3,
                4,
                5,
                6
              ]
            }
          ]
        },
        "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
      },
      "adaptionValueList": {
        "version": "DV_0.0.99",
        "timestamp": "2022-08-11T00:00:00Z",
        "mileage_km": 65432,
        "operatingtime_s": 11570040,
        "values": [
          0.5,
          16554.6,
          234.3,
          323
        ]
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
              "upperLimit": 320,
              "lowerLimit": 0
            }
          ]
        },
        "body": {
          "counts": {
            "countsName": "Time",
            "countsList": [
              769.6,
              15222.4,
              631520.8,
              304929.6,
              196.8
            ]
          },
          "classes": [
            {
              "className": "TC_Clutch-class",
              "classList": [
                2,
                3,
                4,
                5,
                6
              ]
            }
          ]
        },
        "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
      },
      "adaptionValueList": {
        "version": "DV_0.0.99",
        "timestamp": "2022-08-11T00:00:00Z",
        "mileage_km": 65432,
        "operatingtime_s": 11570040,
        "values": [
          0.5,
          16554.6,
          234.3,
          323
        ]
      }
    }
  ]
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = SupplierMockUpDoc.RESPONSE_OK_DESCRIPTION;
    public static final String RESPONSE_OK_VALUE = """
    
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = SupplierMockUpDoc.RESPONSE_ERROR_DESCRIPTION;
    public static final String RESPONSE_ERROR_VALUE = SupplierMockUpDoc.RESPONSE_ERROR_VALUE;
}
