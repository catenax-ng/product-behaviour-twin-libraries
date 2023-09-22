package net.catena_x.btp.hi.oem.backend.hi_service.util;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dao.supplierhiservice.HINotificationFromSupplierContentDAO;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.HINotificationFromSupplierConverter;
import net.catena_x.btp.hi.oem.backend.hi_service.receiver.HIResultProcessor;
import net.catena_x.btp.libraries.edc.util.S3EDCResponseDispatcher;
import net.catena_x.btp.libraries.notification.dao.NotificationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;

@Controller
public class S3EDCResponseDispatcherImpl implements S3EDCResponseDispatcher<HINotificationFromSupplierContentDAO> {

    @Autowired private HIResultProcessor hiResultReceiver;
    @Autowired private HINotificationFromSupplierConverter hiNotificationFromSupplierConverter;

    @Override
    @PostMapping("/edc/notification")
    public ResponseEntity<String> receiveNotification(
            @RequestBody @NotNull final NotificationDAO<HINotificationFromSupplierContentDAO> notificationBody) {
        //System.out.println(notificationBody);
        hiResultReceiver.process(hiNotificationFromSupplierConverter.toDTO(notificationBody),
                ()->{/* Not implemented: Use Http-Endpoint: hidatareceiver/notifyresult */});

        return ResponseEntity.ok().build();
    }
}
