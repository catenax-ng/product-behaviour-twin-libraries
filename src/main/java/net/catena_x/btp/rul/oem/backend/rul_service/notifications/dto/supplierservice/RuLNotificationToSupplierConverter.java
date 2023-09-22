package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.supplierservice;

import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeaderConverter;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.supplierservice.RuLNotificationToSupplierContentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLNotificationToSupplierConverter
        extends DAOConverter<NotificationDAO<RuLNotificationToSupplierContentDAO>,
                             Notification<RuLDataToSupplierContent>> {
    @Autowired private RuLNotificationToSupplierContentConverter rulNotificationToSupplierContentConverter;
    @Autowired private NotificationHeaderConverter notificationHeaderConverter;

    protected Notification<RuLDataToSupplierContent> toDTOSourceExists(
            @NotNull final NotificationDAO<RuLNotificationToSupplierContentDAO> source) {

        return new Notification<RuLDataToSupplierContent>(
                notificationHeaderConverter.toDTO(source.getHeader()),
                rulNotificationToSupplierContentConverter.toDTO(source.getContent()));
    }

    protected NotificationDAO<RuLNotificationToSupplierContentDAO> toDAOSourceExists(
            @NotNull final Notification<RuLDataToSupplierContent> source) {

        return new NotificationDAO<RuLNotificationToSupplierContentDAO>(
                notificationHeaderConverter.toDAO(source.getHeader()),
                rulNotificationToSupplierContentConverter.toDAO(source.getContent()));
    }
}
