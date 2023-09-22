package net.catena_x.btp.hi.supplier.mockup.controller.swagger;

public class SupplierMockUpDirectEndpointDoc {
    public static final String SUMMARY = "MockUp endpoint to simulate the external calculation service.";
    public static final String DESCRIPTION = """
This endpoint is for local tests. It is called manually or by the knowledge agent and calls the notifyresult endpoint. 
""";

    public static final String BODY_DESCRIPTION = SupplierMockUpDoc.BODY_DESCRIPTION;

    public static final String BODY_EXAMPLE_1_NAME = SupplierMockUpDoc.BODY_EXAMPLE_1_NAME;
    public static final String BODY_EXAMPLE_1_DESCRIPTION = SupplierMockUpDoc.BODY_EXAMPLE_1_DESCRIPTION;
    public static final String BODY_EXAMPLE_1_VALUE = SupplierMockUpDoc.BODY_EXAMPLE_1_VALUE;

    public static final String RESPONSE_OK_DESCRIPTION = SupplierMockUpDoc.RESPONSE_OK_DESCRIPTION;
    public static final String RESPONSE_OK_VALUE = SupplierMockUpDoc.RESPONSE_OK_VALUE;

    public static final String RESPONSE_ERROR_DESCRIPTION = SupplierMockUpDoc.RESPONSE_ERROR_DESCRIPTION;
    public static final String RESPONSE_ERROR_VALUE = SupplierMockUpDoc.RESPONSE_ERROR_VALUE;
}
