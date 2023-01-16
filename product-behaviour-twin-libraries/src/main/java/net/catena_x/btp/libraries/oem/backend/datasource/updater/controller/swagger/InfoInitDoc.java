package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger;

public final class InfoInitDoc {
    public static final String SUMMARY = "Initializes the info elements to their default values.";
    public static final String DESCRIPTION = """
The info elements must not exist.
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: All info items are initialized.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-09T17:18:01.434285Z",
  "result": "Ok",
  "message": ""
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Info items are not initialized. " +
            "Maybe they were already present.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-19T14:47:00.956151Z",
  "result": "Error",
  "message": "net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException: Inserting info value failed!"
}
""";
}
