package net.catena_x.btp.libraries.edc.util;

import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;

public interface S3EDCResponseDispatcher<ContentTypeDAO> {
    ResponseEntity<String> receiveNotification(
            @NotNull final NotificationDAO<ContentTypeDAO> notificationBody);
}
