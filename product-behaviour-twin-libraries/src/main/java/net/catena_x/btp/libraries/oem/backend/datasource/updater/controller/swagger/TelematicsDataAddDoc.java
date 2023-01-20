package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger;

public final class TelematicsDataAddDoc {
    public static final String SUMMARY = "Adds new telematics data for a vehicle.";
    public static final String DESCRIPTION = """
The related vehicles have to be registered before telematics data can be uploaded for them.
If the given telematics data are not newer than the last uploaded data, the new data are ignored.
""";
    public static final String BODY_DESCRIPTION = "Sample telematics data";

    public static final String BODY_EXAMPLE_1_NAME = "test data";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Test telematics data for the sample vehicle.";
    public static final String BODY_EXAMPLE_1_VALUE ="""
{
  "vehicleId": "urn:uuid:fb4f48c7-bc13-4148-ad22-d37a8fa63289",
  "loadSpectra": [
    {
      "targetComponentID": "urn:uuid:13673040-413a-44e1-b1bd-ab09125da515",
      "metadata": {
        "projectDescription": "projectnumber Stadt",
        "componentDescription": "GearSet",
        "routeDescription": "logged",
        "status": {
          "date": "2022-11-29T09:16:47.450911100Z",
          "operatingHours": 3213.9,
          "mileage": 65432
        }
      },
      "header": {
        "countingValue": null,
        "countingUnit": "unit:ONE",
        "countingMethod": "LRD",
        "channels": [
          {
            "unit": "unit:rpm",
            "numberOfBins": 128,
            "channelName": "N_TU",
            "upperLimit": 12700.0,
            "lowerLimit": -100.0
          },
          {
            "unit": "unit:Nm",
            "numberOfBins": 128,
            "channelName": "T_TU",
            "upperLimit": 1290.0,
            "lowerLimit": -1270.0
          },
          {
            "unit": "unit:ONE",
            "numberOfBins": 10,
            "channelName": "Z_GANG",
            "upperLimit": 9.5,
            "lowerLimit": -0.5
          }
        ]
      },
      "body": {
        "counts": {
          "countsName": "Counts",
          "countsList": [0.6996546387672424, 2.277406692504883, 3.5231547355651855, 13.261415481567383, 28.902271270751953]
        },
        "classes": [
          {
            "className": "N_TU-class",
            "classList": [1, 1, 1, 2, 3]
          },
          {
            "className": "T_TU-class",
            "classList": [68, 64, 67, 69, 71]
          },
          {
            "className": "Z_GANG-class",
            "classList": [1, 1, 1, 1, 1]
          }
        ]
      }
    },
    {
      "targetComponentID": "urn:uuid:13673040-413a-44e1-b1bd-ab09125da515",
      "metadata": {
        "projectDescription": "projectnumber Stadt",
        "componentDescription": "GearOil",
        "routeDescription": "logged",
        "status": {
          "date": "2022-11-29T09:16:47.450911100Z",
          "operatingHours": 3213.9,
          "mileage": 65432
        }
      },
      "header": {
        "countingValue": null,
        "countingUnit": "unit:ONE",
        "countingMethod": "LRD",
        "channels": [
          {
            "unit": "unit:rpm",
            "numberOfBins": 128,
            "channelName": "N_TU",
            "upperLimit": 12700.0,
            "lowerLimit": -100.0
          },
          {
            "unit": "unit:Nm",
            "numberOfBins": 128,
            "channelName": "T_TU",
            "upperLimit": 1290.0,
            "lowerLimit": -1270.0
          },
          {
            "unit": "unit:ONE",
            "numberOfBins": 10,
            "channelName": "Z_GANG",
            "upperLimit": 9.5,
            "lowerLimit": -0.5
          }
        ]
      },
      "body": {
        "counts": {
          "countsName": "Counts",
          "countsList": [0.6996546387672424, 2.277406692504883, 3.5231547355651855, 13.261415481567383, 28.902271270751953]
        },
        "classes": [
          {
            "className": "N_TU-class",
            "classList": [1, 1, 1, 2, 3]
          },
          {
            "className": "T_TU-class",
            "classList": [68, 64, 67, 69, 71]
          },
          {
            "className": "Z_GANG-class",
            "classList": [1, 1, 1, 1, 1]
          }
        ]
      }
    },
    {
      "targetComponentID": "urn:uuid:13673040-413a-44e1-b1bd-ab09125da515",
      "metadata": {
        "projectDescription": "projectnumber Landstrasse",
        "componentDescription": "Clutch",
        "routeDescription": "logged",
        "status": {
          "date": "2022-08-21",
          "operatingHours": 1281.9,
          "mileage": 76543
        }
      },
      "header": {
        "countingMethod": "TimeAtLevel",
        "channels": [
          {
            "channelName": "TC_Clutch",
            "unit": "unit:degreeCelsius",
            "lowerLimit": 0.0,
            "upperLimit": 320.0,
            "numberOfBins": 8
          }
        ],
        "countingValue": "Time",
        "countingUnit": "unit:secondUnitOfTime"
      },
      "body": {
        "classes": [
          {
            "className": "TC_Clutch-class",
            "classList": [2, 3, 4, 5, 6]
          }
        ],
        "counts": {
          "countsName": "Time",
          "countsList": [3018.21, 3451252.83, 699160.662, 349580.331, 116526.777]
        }
      }
    }
  ],
  "adaptionValues": [
    {
      "status": {
        "date": "2022-11-29T09:16:47.450911100Z",
        "operatingHours": 3213.9,
        "mileage": 65432
      },
      "values": [0.3, 45.2, 11.0, 0.4]
    }
  ]
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The telematics data were added successfully or are ignored.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T15:51:01.990821500Z",
  "result": "Ok",
  "message": "Telematics data added."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: The telematics data could not be added.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T17:41:53.292113600Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException: Vehicle urn:uuid:fb4f48c7-bc13-4148-ad22-dd37a8fa63289 does not exist!"
}
""";
}
