package net.catena_x.btp.libraries.notification.dto.items;

import net.catena_x.btp.libraries.notification.dao.items.NotificationHeaderDAO;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class NotificationHeaderConverter extends DAOConverter<NotificationHeaderDAO, NotificationHeader> {

    protected NotificationHeader toDTOSourceExists(@NotNull final NotificationHeaderDAO source) {
        return new NotificationHeader(
                source.getNotificationID(),
                source.getReferencedNotificationID(),
                source.getRelatedNotificationID(),
                source.getSenderBPN(),
                source.getSenderAddress(),
                source.getRecipientBPN(),
                source.getRecipientAddress(),
                source.getClassification(),
                source.getSeverity(),
                source.getStatus(),
                source.getTargetDate(),
                source.getTimeStamp());
    }

    protected NotificationHeaderDAO toDAOSourceExists(@NotNull final NotificationHeader source) {
        return new NotificationHeaderDAO(
                source.getNotificationID(),
                source.getReferencedNotificationID(),
                source.getRelatedNotificationID(),
                source.getSenderBPN(),
                source.getSenderAddress(),
                source.getRecipientBPN(),
                source.getRecipientAddress(),
                source.getClassification(),
                source.getSeverity(),
                source.getStatus(),
                source.getTargetDate(),
                source.getTimeStamp());
    }
}
