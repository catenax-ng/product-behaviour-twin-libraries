package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class InitReinitByFileDoc {
    public static final String SUMMARY = "Initializes digital twins from file.";
    public static final String DESCRIPTION = """
Uploads a file with digital twin definitions. There have to be corresponding vehicles and gearboxes per file.
There can be more than one sets in the file. The internally stored digital twins are reset and the new digital twins
are stored internally as new test data.
All testdata must be initialized before registering vehicles and telematics data. """;

    public static final String BODY_DESCRIPTION = "Sample test data (list of digital twins).";

    public static final String BODY_EXAMPLE_1_NAME = "Sample vehicle and gearbox";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "A sample set of a vehicle and a gearbox.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
  "digitalTwins": [
    {
      "catenaXId": "urn:uuid:79d19614-b699-4716-b232-ef250e1c1773",
      "urn:bamm:io.catenax.serial_part_typization:1.1.0#SerialPartTypization": [
        {
          "catenaXId": "urn:uuid:79d19614-b699-4716-b232-ef250e1c1773",
          "localIdentifiers": [
            {
              "key": "manufacturerId",
              "value": "BPNL00000003AYRE"
            },
            {
              "key": "manufacturerPartId",
              "value": "CL-97"
            },
            {
              "key": "partInstanceId",
              "value": "FNLQNRVCOFLHAQ"
            },
            {
              "key": "van",
              "value": "FNLQNRVCOFLHAQ"
            }
          ],
          "manufacturingInformation": {
            "date": "2019-08-24T23:21:39Z",
            "country": "DEU"
          },
          "partTypeInformation": {
            "classification": "product",
            "manufacturerPartId": "CL-97",
            "nameAtManufacturer": "Vehicle Combustion",
            "customerPartId": null,
            "nameAtCustomer": null
          },
          "bammId": "urn:bamm:io.catenax.serial_part_typization:1.1.0#SerialPartTypization"
        }
      ],
      "urn:bamm:io.catenax.assembly_part_relationship:1.1.1#AssemblyPartRelationship": [
        {
          "catenaXId": "urn:uuid:79d19614-b699-4716-b232-ef250e1c1773",
          "childParts": [
            {
              "childCatenaXId": "urn:uuid:4773625a-5e56-4879-abed-475be29bd663",
              "quantity": {
                "quantityNumber": 1,
                "measurementUnit": {
                  "datatypeURI": "urn:bamm:io.openmanufacturing:meta-model:1.0.0#curie",
                  "lexicalValue": "unit:piece"
                }
              },
              "lifecycleContext": "AsBuilt",
              "assembledOn": "2022-02-03T14:48:54.709Z",
              "lastModifiedOn": "2022-02-03T14:48:54.709Z"
            }
          ],
          "bammId": "urn:bamm:io.catenax.assembly_part_relationship:1.1.1#AssemblyPartRelationship"
        }
      ],
      "urn:bamm:io.catenax.rul:1.0.0##RemainingUsefulLife": [
        {
          "remainingOperatingHours": 20000,
          "remainingRunningDistance": 1000000,
          "determinationLoaddataSource": {
            "informationOriginLoadSpectrum": "loggedOEM",
            "informationLoadSpectrum": null
          },
          "determinationStatus": {
            "date": null,
            "operatingHours": 0,
            "mileage": 0
          },
          "bammId": "urn:bamm:io.catenax.rul:1.0.0##RemainingUsefulLife"
        }
      ],
      "urn:bamm:io.catenax.damage:1.0.0#Damage": [
        {
          "damageValue": 0.0,
          "determinationLoaddataSource": {
            "informationOriginLoadSpectrum": "loggedOEM",
            "informationLoadSpectrum": null
          },
          "determinationMethod": "Arrhenius",
          "determinationStatus": {
            "date": null,
            "operatingHours": 0,
            "mileage": 0
          },
          "bammId": "urn:bamm:io.catenax.damage:1.0.0#Damage"
        }
      ],
      "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum": [
        {
          "targetComponentID": "urn:uuid:4773625a-5e56-4879-abed-475be29bd664",
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
                "classList": [68, 64, 67, 69, 71, 71]
              },
              {
                "className": "Z_GANG-class",
                "classList": [1, 1, 1, 1, 1]
              }
            ]
          },
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
        },
        {
          "targetComponentID": "urn:uuid:4773625a-5e56-4879-abed-475be29bd664",
          "metadata": {
            "projectDescription": "projectnumber Stadt",
            "componentDescription": "GearOil",
            "routeDescription": "logged",
            "status": {
              "date": null,
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
                "numberOfBins": 128,
                "channelName": "TC_SU",
                "upperLimit": 640.0,
                "lowerLimit": 0.0
              }
            ]
          },
          "body": {
            "counts": {
              "countsName": "Time",
              "countsList": [
                58051.140625,
                122800.109375,
                666214.3125,
                7761702.5,
                4194847.5,
                1435927.25,
                1557759.375,
                3185557.5,
                224753.84375
              ]
            },
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
            ]
          },
          "bammId": "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum"
        },
        {
          "targetComponentID": "urn:uuid:84db4c59-5fa1-4266-9e4c-94bea3cf72a4",
          "metadata": {
            "projectDescription": "projectnumber BAB",
            "componentDescription": "Clutch",
            "routeDescription": "logged",
            "status": {
              "date": null,
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
        }
      ],
      "urn:bamm:mockup.digitaltwin:1.0.0#AdaptionValues": [
        {
          "status": {
            "date": "2022-11-29T09:16:47.450911100Z",
            "operatingHours": 3213.9,
            "mileage": 65432
          },
          "values": [
            0.5,
            16554.6,
            234.3,
            323.0
          ]
        }
      ]
    },
    {
      "catenaXId": "urn:uuid:4773625a-5e56-4879-abed-475be29bd663",
      "urn:bamm:io.catenax.serial_part_typization:1.1.0#SerialPartTypization": [
        {
          "catenaXId": "urn:uuid:4773625a-5e56-4879-abed-475be29bd663",
          "localIdentifiers": [
            {
              "key": "manufacturerId",
              "value": "BPNL00000003B2OM"
            },
            {
              "key": "manufacturerPartId",
              "value": "32494586-73"
            },
            {
              "key": "partInstanceId",
              "value": "NO-119279485335487505977441"
            }
          ],
          "manufacturingInformation": {
            "date": "2022-02-04T14:48:54Z",
            "country": "DEU"
          },
          "partTypeInformation": {
            "classification": "component",
            "manufacturerPartId": "32494586-73",
            "nameAtManufacturer": "Differential Gear",
            "customerPartId": "32494586-73",
            "nameAtCustomer": "Differential Gear"
          },
          "bammId": "urn:bamm:io.catenax.serial_part_typization:1.1.0#SerialPartTypization"
        }
      ],
      "urn:bamm:io.catenax.assembly_part_relationship:1.1.1#AssemblyPartRelationship": null,
      "urn:bamm:io.catenax.rul:1.0.0##RemainingUsefulLife": null,
      "urn:bamm:io.catenax.damage:1.0.0#Damage": null,
      "urn:bamm:io.openmanufacturing.digitaltwin:1.0.0#ClassifiedLoadSpectrum": null,
      "urn:bamm:mockup.digitaltwin:1.0.0#AdaptionValues": null
    }
  ],
  "clutchLoadSpectrumGreen": null,
  "clutchLoadSpectrumYellow": null,
  "clutchLoadSpectrumRed": null
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Digital twins successfully initialized.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T19:15:30.930459200Z",
  "result": "Ok",
  "message": "Testdata reinitialized."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Initializing failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:57:31.399822300Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: Error while reading testdata from json string!"
}
""";
}
