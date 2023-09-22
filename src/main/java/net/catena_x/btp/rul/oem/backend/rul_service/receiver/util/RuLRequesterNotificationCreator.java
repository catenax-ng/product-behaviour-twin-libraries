package net.catena_x.btp.rul.oem.backend.rul_service.receiver.util;

import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeader;
import net.catena_x.btp.libraries.notification.enums.NFSeverity;
import net.catena_x.btp.libraries.notification.enums.NFStatus;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester.RuLDataToRequesterContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;

@Component
public class RuLRequesterNotificationCreator {
    @Value("${edc.bpn}") private String edcBpn;
    @Value("${edc.endpoint}") private String edcEndpoint;
    @Value("${requester.rulservice.classification}") private String rulResultClassification;

    public Notification<RuLDataToRequesterContent> createForHttp(
            @NotNull final String requesterNotificationId,
            @NotNull final RuLDataToRequesterContent rulDataToRequesterContent,
            @NotNull final EdcAssetAddress requesterAssetAddress) {

        final Notification<RuLDataToRequesterContent> notification = new Notification<>();
        notification.setHeader(createHeader(requesterNotificationId, requesterAssetAddress));
        notification.setContent(rulDataToRequesterContent);
        return notification;
    }

    private NotificationHeader createHeader(@NotNull final String requesterNotificationId,
                                            @NotNull final EdcAssetAddress requesterAssetAddress) {
        final NotificationHeader header = new NotificationHeader();
        header.setReferencedNotificationID(requesterNotificationId);

        setSenderData(header);
        setRecipientData(header, requesterAssetAddress);
        setRequestDependentData(header);

        return header;
    }

    private void setSenderData(@NotNull final NotificationHeader headerInOut) {
        headerInOut.setSenderAddress(edcEndpoint);
        headerInOut.setSenderBPN(edcBpn);
    }

    private void setRecipientData(@NotNull final NotificationHeader headerInOut,
                                  @NotNull final EdcAssetAddress requesterAssetAddress) {
        headerInOut.setRecipientAddress(requesterAssetAddress.getConnectorUrl());
        headerInOut.setRecipientBPN(requesterAssetAddress.getBpn());
    }

    private void setRequestDependentData(@NotNull final NotificationHeader headerInOut) {
        headerInOut.setClassification(rulResultClassification);
        headerInOut.setSeverity(NFSeverity.MINOR);
        headerInOut.setStatus(NFStatus.SENT);
        headerInOut.setTimeStamp(Instant.now());
        headerInOut.setTargetDate(headerInOut.getTimeStamp().plus(Duration.ofHours(12L)));
    }
}
