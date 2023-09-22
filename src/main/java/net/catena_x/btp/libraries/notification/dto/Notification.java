package net.catena_x.btp.libraries.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.notification.dto.items.NotificationHeader;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification<ContentType> {
    private NotificationHeader header;
    private ContentType content;
}
