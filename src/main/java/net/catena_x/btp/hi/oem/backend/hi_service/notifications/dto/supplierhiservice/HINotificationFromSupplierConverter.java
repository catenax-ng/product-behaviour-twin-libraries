package net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationFromSupplierContentDAO;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeaderConverter;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HINotificationFromSupplierConverter
        extends DAOConverter<NotificationDAO<HINotificationFromSupplierContentDAO>,
        Notification<HINotificationFromSupplierContent>> {

    @Autowired private HINotificationFromSupplierContentConverter HINotificationFromSupplierContentConverter;
    @Autowired private NotificationHeaderConverter notificationHeaderConverter;

    protected Notification<HINotificationFromSupplierContent> toDTOSourceExists(
            @NotNull final NotificationDAO<HINotificationFromSupplierContentDAO> source) {

        return new Notification<HINotificationFromSupplierContent>(
                notificationHeaderConverter.toDTO(source.getHeader()),
                HINotificationFromSupplierContentConverter.toDTO(source.getContent()));
    }

    protected NotificationDAO<HINotificationFromSupplierContentDAO> toDAOSourceExists(
            @NotNull final Notification<HINotificationFromSupplierContent> source) {

        return new NotificationDAO<HINotificationFromSupplierContentDAO>(
                notificationHeaderConverter.toDAO(source.getHeader()),
                HINotificationFromSupplierContentConverter.toDAO(source.getContent()));
    }
}
