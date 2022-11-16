package net.catena_x.btp.libraries.oem.backend.util;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class S3DataPlaneEDCInitiator {
    // This is only responsible for sending the notification
    // Make dispatcher class to get response
    // Third class should store request id (requestMapper)
    public <T> void startAsyncRequest(String requestId, String endpoint, String messageBody,
                                      Consumer<T> responseHandler,
                                      Class<T> expectedResponseClass) {
        // TODO: Don't use functional interfaces, hard code response behaviour instead
        //  delete consumer and class
    }
}
