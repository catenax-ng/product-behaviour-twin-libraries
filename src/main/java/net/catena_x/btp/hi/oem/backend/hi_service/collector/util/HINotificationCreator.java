package net.catena_x.btp.hi.oem.backend.hi_service.collector.util;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.HIDataToSupplierContent;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.enums.NotificationClassification;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeader;
import net.catena_x.btp.libraries.notification.enums.NFSeverity;
import net.catena_x.btp.libraries.notification.enums.NFStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;

@Component
public class HINotificationCreator {
    @Value("${supplier.hiservice.endpoint}") private URL suplierHiServiceEndpoint;
    @Value("${supplier.hiservice.bpn}") private String supplierBpn;
    @Value("${edc.bpn}") private String edcBpn;
    @Value("${edc.endpoint}") private URL edcEndpoint;

    public Notification<HIDataToSupplierContent> createForHttp(
            @NotNull final String requestId,
            @NotNull final HIDataToSupplierContent hiDataToSupplierContent) {

        final Notification<HIDataToSupplierContent> notification = new Notification<>();
        notification.setHeader(createHeader(requestId));
        notification.setContent(hiDataToSupplierContent);
        return notification;
    }

    private NotificationHeader createHeader(@NotNull final String requestId) {

        final NotificationHeader header = new NotificationHeader();

        header.setNotificationID(requestId);

        setSenderData(header);
        setRecipientData(header);
        setRequestDependentData(header);

        return header;
    }

    private void setSenderData(@NotNull final NotificationHeader headerInOut) {
        headerInOut.setSenderBPN(edcBpn);
        headerInOut.setSenderAddress(edcEndpoint.toString());
    }

    private void setRecipientData(@NotNull final NotificationHeader headerInOut) {
        headerInOut.setRecipientBPN(supplierBpn);
        headerInOut.setRecipientAddress(suplierHiServiceEndpoint.toString());
    }

    private void setRequestDependentData(@NotNull final NotificationHeader headerInOut) {
        headerInOut.setClassification(NotificationClassification.HISERVICE.toString());
        headerInOut.setSeverity(NFSeverity.MINOR);
        headerInOut.setStatus(NFStatus.SENT);
        headerInOut.setTimeStamp(Instant.now());
        headerInOut.setTargetDate(headerInOut.getTimeStamp().plus(Duration.ofHours(12L)));
    }
}
