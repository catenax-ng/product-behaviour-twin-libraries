package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.requester.RuLNotificationFromRequesterContentDAO;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLNotificationFromRequesterContentConverter
        extends DAOConverter<RuLNotificationFromRequesterContentDAO, RuLNotificationFromRequesterContent> {

    protected RuLNotificationFromRequesterContent toDTOSourceExists(
            @NotNull final RuLNotificationFromRequesterContentDAO source) {

        return new RuLNotificationFromRequesterContent(source.getVin());
    }

    protected RuLNotificationFromRequesterContentDAO toDAOSourceExists(
            @NotNull final RuLNotificationFromRequesterContent source) {

        return new RuLNotificationFromRequesterContentDAO(source.getVin());
    }
}
