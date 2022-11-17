package net.catena_x.btp.libraries.oem.backend.util;

import org.springframework.stereotype.Component;

@Component
public interface S3EDCInitiator {
    void startAsyncRequest(String requestId, String endpoint, String messageBody);
}
