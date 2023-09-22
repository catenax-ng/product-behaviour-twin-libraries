package net.catena_x.btp.libraries.oem.backend.datasource.provider.controller.swagger;

public final class SqlExportByConfigDoc {
    public static final String SUMMARY = "Exports digital twins from a config file to SQL.";
    public static final String DESCRIPTION = """
Uploads a config file with digital twin configurations.
The digital twins are then exported to SQL. The data are not registered.
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

    public static final String SCHEMA_NAME_NAME = "schemaname";
    public static final String SCHEMA_NAME_DESCRIPTION = "Sets the schema name for the SQL export.";
    public static final String SCHEMA_NAME_EXAMPLE_1_NAME = "local test database";
    public static final String SCHEMA_NAME_EXAMPLE_1_DESCRIPTION = "User name for local test database.";
    public static final String SCHEMA_NAME_EXAMPLE_1_VALUE = "hi";
    public static final String SCHEMA_NAME_EXAMPLE_2_NAME = "BMW database";
    public static final String SCHEMA_NAME_EXAMPLE_2_DESCRIPTION = "User name for BMW database.";
    public static final String SCHEMA_NAME_EXAMPLE_2_VALUE = "behaviour_twin";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Digital twins successfully exported. SQL is embedded.";
    public static final String RESPONSE_OK_VALUE = """
COPY hi.info (key, query_timestamp, value) FROM stdin;
DATAVERSION	\\N	DV_0.0.99
LOADSPECTRUMINFO	\\N	{}
ADAPTIONVALUEINFO	\\N	{"names" : [ "AV1", "AV2", "AV3", "AV4" ]}
\\.

COPY hi.sync (id, query_timestamp, sync_counter) FROM stdin;
DEFAULT	2023-02-06 12:53:39.659029	0
\\.

COPY hi.telematics_data (id, adaption_values, load_spectra, storage_timestamp, sync_counter, vehicle_id) FROM stdin;
...""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Exporting failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T18:58:05.631718900Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException: Error while reading testdata from json string!"
}
""";
}
