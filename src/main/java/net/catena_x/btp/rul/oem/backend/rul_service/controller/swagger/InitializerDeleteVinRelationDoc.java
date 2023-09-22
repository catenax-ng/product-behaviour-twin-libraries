package net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger;

public final class InitializerDeleteVinRelationDoc {
    public static final String SUMMARY = "Deletes a VIN-refId-relation of a vehicle.";
    public static final String DESCRIPTION = """
Deletes a VIN-refId-relation of a vehicle.
""";

    public static final String VIN_DESCRIPTION = "Vehicles VIN.";

    public static final String VIN_EXAMPLE_1_NAME = "Vehicle 1";
    public static final String VIN_EXAMPLE_1_DESCRIPTION = "VIN of vehicle with RuL > 100.000km.";
    public static final String VIN_EXAMPLE_1_VALUE = "WBAVP11070VR72001";

    public static final String VIN_EXAMPLE_2_NAME = "Vehicle 2";
    public static final String VIN_EXAMPLE_2_DESCRIPTION = "VIN of vehicle with RuL < 100.000km.";
    public static final String VIN_EXAMPLE_2_VALUE = "WBAVP11070VR72002";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The relation was deleted successfully.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-22T11:12:22.542895Z",
  "result": "Ok",
  "message": "VIN relation deleted successfully."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: The relation could not be deleted.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-22T15:42:26.019400Z",
  "result": "Error",
  "message": "VIN relation deletion failed: Deleting VIN relation failed: Relation for given vin does not exist!"
}
""";
}