package net.catena_x.btp.libraries.oem.backend.datasource.updater.controller.swagger;

public final class InfoSetDoc {
    public static final String SUMMARY = "Sets an info item.";
    public static final String DESCRIPTION = """
The new info item must not already exist.
The default workflow is not to use this endpoint directly. Instead, the endpoint /api/rawdata/info/init should be used. 
""";
    public static final String BODY_DESCRIPTION = "Info item to be inserted.";

    public static final String BODY_EXAMPLE_1_NAME = "version example";
    public static final String BODY_EXAMPLE_1_DESCRIPTION = "Sets the data version element.";
    public static final String BODY_EXAMPLE_1_VALUE ="""
{
  "key": "DATAVERSION",
  "value": "DV_0.0.99"
}
""";

    public static final String BODY_EXAMPLE_2_NAME = "adaption values info example";
    public static final String BODY_EXAMPLE_2_DESCRIPTION = "Sets the adaption values info element.";
    public static final String BODY_EXAMPLE_2_VALUE = """
{
  "key": "ADAPTIONVALUEINFO",
  "value": "{\\"names\\" : [ \\"AV1\\", \\"AV2\\", \\"AV3\\", \\"AV4\\" ]}"
}
""";

    public static final String BODY_EXAMPLE_3_NAME = "load spectra info example";
    public static final String BODY_EXAMPLE_3_DESCRIPTION = "Sets the load spectra info element.";
    public static final String BODY_EXAMPLE_3_VALUE = """
{
  "key": "LOADSPECTRUMINFO",
  "value": "{}"
}
""";

    public static final String RESPONSE_OK_DESCRIPTION = "OK: Setting data version successful.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-09T17:18:01.434285Z",
  "result": "Ok",
  "message": "DATAVERSION set to \\"DV_0.0.99\\""
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: Setting data version element failed.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-09T16:24:52.741984700Z",
  "result": "Error",
  "message": "java.lang.IllegalArgumentException: No enum constant net.catena_x.btp.libraries.oem.backend.model.enums.InfoKey.string"
}
""";
}
