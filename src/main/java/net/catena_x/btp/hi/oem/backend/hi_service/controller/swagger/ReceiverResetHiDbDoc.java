package net.catena_x.btp.hi.oem.backend.hi_service.controller.swagger;

public final class ReceiverResetHiDbDoc {
    public static final String SUMMARY = "Resets the health indicators database.";
    public static final String DESCRIPTION = """
Resets the health indicators database. Clears all registered vehicles and their health data.
""";


    public static final String RESPONSE_OK_DESCRIPTION = "OK: database reset.";
    public static final String RESPONSE_OK_VALUE = """
{
  "timestamp": "2022-12-20T10:55:12.734354400Z",
  "result": "Ok",
  "message": "Hi database reset."
}
""";

    public static final String RESPONSE_ERROR_DESCRIPTION = "ERROR: DESCRIPTION.";
    public static final String RESPONSE_ERROR_VALUE = """
{
  "timestamp": "2022-12-20T10:56:50.246581200Z",
  "result": "Error",
  "message": "Could not open JPA EntityManager for transaction; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection"
}
""";
}
