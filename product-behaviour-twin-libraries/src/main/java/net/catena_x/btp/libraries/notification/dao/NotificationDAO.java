package net.catena_x.btp.libraries.notification.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.catena_x.btp.libraries.notification.dao.items.NotificationHeaderDAO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDAO<ContentTypeDAO> {
    private NotificationHeaderDAO header;
    private ContentTypeDAO content;
}
