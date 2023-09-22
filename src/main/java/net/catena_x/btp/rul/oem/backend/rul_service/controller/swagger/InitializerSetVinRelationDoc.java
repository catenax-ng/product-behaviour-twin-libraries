package net.catena_x.btp.rul.oem.backend.rul_service.controller.swagger;

import net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation.RuLVinRelationTableInternal;

public final class InitializerSetVinRelationDoc {
    public static final String SUMMARY = "Sets a VIN-refId-relation for a vehicle.";
    public static final String DESCRIPTION = """
Sets a VIN-refId-relation for a vehicle. 
""";

    public static final String VIN_DESCRIPTION = "Vehicles VIN.";

    public static final String VIN_EXAMPLE_1_NAME = "Vehicle 1";
    public static final String VIN_EXAMPLE_1_DESCRIPTION = "VIN of vehicle with RuL > 100.000km.";
    public static final String VIN_EXAMPLE_1_VALUE = "WBAVP11070VR72001";

    public static final String VIN_EXAMPLE_2_NAME = "Vehicle 2";
    public static final String VIN_EXAMPLE_2_DESCRIPTION = "VIN of vehicle with RuL < 100.000km.";
    public static final String VIN_EXAMPLE_2_VALUE = "WBAVP11070VR72002";

    public static final String VIN_EXAMPLE_3_NAME = "Vehicle 3";
    public static final String VIN_EXAMPLE_3_DESCRIPTION = "VIN of vehicle with no data.";
    public static final String VIN_EXAMPLE_3_VALUE = "WBAVP11070VR72004";

    public static final String REFID_DESCRIPTION = "Vehicles ref-id (e.g. cantena-x-id or AAS-id).";

    public static final String REFID_EXAMPLE_1_NAME = "Ref-id vehicle 1";
    public static final String REFID_EXAMPLE_1_DESCRIPTION = "Ref-id of vehicle with RuL > 100.000km.";
    public static final String REFID_EXAMPLE_1_VALUE = "urn:uuid:00e5d4e8-d442-4cd3-aaea-b21c4e352c81";

    public static final String REFID_EXAMPLE_2_NAME = "Ref-id vehicle 2";
    public static final String REFID_EXAMPLE_2_DESCRIPTION = "Ref-id of vehicle with RuL < 100.000km.";
    public static final String REFID_EXAMPLE_2_VALUE = "urn:uuid:01b4a86e-9c7e-4082-bc69-4f8c0a13c178";

    public static final String REFID_EXAMPLE_3_NAME = "Ref-id vehicle 3 (no data)";
    public static final String REFID_EXAMPLE_3_DESCRIPTION = "Ref-id of vehicle with no data.";
    public static final String REFID_EXAMPLE_3_VALUE = RuLVinRelationTableInternal.NO_DATA_REF;

    public static final String RESPONSE_OK_DESCRIPTION = "OK: The relation was set successfully.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-22T12:40:31.308803600Z",
  "result": "Ok",
  "message": "VIN relation set successfully."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: The relation could not be set.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-22T11:31:02.403791400Z",
  "result": "Ok",
  "message": "Setting VIN relation failed: Inserting VIN relation failed: Vehicle with given ref-id does not exist in rawdata database!"
}
""";
}