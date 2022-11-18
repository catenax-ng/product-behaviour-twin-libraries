package net.catena_x.btp.libraries.notification.dto.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.notification.enums.NFSeverity;
import net.catena_x.btp.libraries.notification.enums.NFStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHeader {
    private String notificationID;
    private String referencedNotificationID;
    private String relatedNotificationID;
    private String senderBPN;
    private String senderAddress;
    private String recipientBPN;
    private String recipientAddress;
    private String classification;
    private NFSeverity severity;
    private NFStatus status;
    private Instant targetDate;
    private Instant timeStamp;
}
