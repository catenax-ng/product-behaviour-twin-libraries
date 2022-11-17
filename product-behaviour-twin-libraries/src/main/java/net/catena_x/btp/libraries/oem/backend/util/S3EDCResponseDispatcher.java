package net.catena_x.btp.libraries.oem.backend.util;

import org.springframework.http.ResponseEntity;

public interface S3EDCResponseDispatcher {
    ResponseEntity<String> receiveNotification(String notificationBody);
}
