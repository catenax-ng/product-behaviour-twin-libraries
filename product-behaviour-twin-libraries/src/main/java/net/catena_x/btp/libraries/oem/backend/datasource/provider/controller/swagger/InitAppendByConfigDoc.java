package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class InitAppendByConfigDoc {
    public static final String SUMMARY = "Appends digital twins from file.";
    public static final String DESCRIPTION = """
Uploads a config file with digital twin configurations.
The digital twins are appended to the internally stored test data.
All testdata must be initialized before registering vehicles and telematics data. 
""";
    public static final String BODY_DESCRIPTION = "List of digital twins.";

    public static final String BODY_EXAMPLE_1_NAME = "Sample config for RuL use case";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "A sample  config for RuL use case.";
    public static final String BODY_EXAMPLE_1_VALUE = """
{
     "rulTestDataFiles": [
         {
             "filename": "Test-GearOil_classification.json",
             "vehicle" : {
                 "vin": "VIN_01",
                 "van": "VAN_01",
                 "catenaxId" : "urn:uuid:78b2073e-b92a-4a57-bfd0-7f4b455884f0",
                 "manufacturerId" : "TF-38",
                 "manufacturingDate" : "2022-02-03T14:48:54.709Z"
             },
             "gearbox" : {
                 "catenaxId" : "urn:uuid:d77a48ee-5235-4f5f-84b0-5e9e2d324517",
                 "manufacturerId" : "32494586-74",
                 "manufacturingDate" : "2022-01-03T14:48:54.709Z",
                 "assembledOn" : "2022-02-02T14:48:54.709Z"
             }
         }
     ]
 }
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Digital twins successfully appended.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-19T19:14:26.879452900Z",
  "result": "Ok",
  "message": "Testdata appended."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Appending failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:58:05.631718900Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: Error while reading testdata from json string!"
}
""";
}
