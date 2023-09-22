package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester;

import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeaderConverter;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.requester.RuLNotificationToRequesterContentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLNotificationToRequesterConverter
        extends DAOConverter<NotificationDAO<RuLNotificationToRequesterContentDAO>,
                             Notification<RuLDataToRequesterContent>> {
    @Autowired
    private RuLNotificationToRequesterContentConverter rulNotificationToRequesterContentConverter;
    @Autowired private NotificationHeaderConverter notificationHeaderConverter;

    protected Notification<RuLDataToRequesterContent> toDTOSourceExists(
            @NotNull final NotificationDAO<RuLNotificationToRequesterContentDAO> source) {

        return new Notification<RuLDataToRequesterContent>(
                notificationHeaderConverter.toDTO(source.getHeader()),
                rulNotificationToRequesterContentConverter.toDTO(source.getContent()));
    }

    protected NotificationDAO<RuLNotificationToRequesterContentDAO> toDAOSourceExists(
            @NotNull final Notification<RuLDataToRequesterContent> source) {

        return new NotificationDAO<RuLNotificationToRequesterContentDAO>(
                notificationHeaderConverter.toDAO(source.getHeader()),
                rulNotificationToRequesterContentConverter.toDAO(source.getContent()));
    }
}
