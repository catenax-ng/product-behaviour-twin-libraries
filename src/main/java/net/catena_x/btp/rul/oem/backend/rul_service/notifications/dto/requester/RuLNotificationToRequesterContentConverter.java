package net.catena_x.btp.rul.oem.backend.rul_service.notifications.dto.requester;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.rul_service.notifications.dao.requester.RuLNotificationToRequesterContentDAO;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class RuLNotificationToRequesterContentConverter
        extends DAOConverter<RuLNotificationToRequesterContentDAO, RuLDataToRequesterContent> {
    protected RuLDataToRequesterContent toDTOSourceExists(
            @NotNull final RuLNotificationToRequesterContentDAO source) {

        return new RuLDataToRequesterContent(source.getRul());
    }

    protected RuLNotificationToRequesterContentDAO toDAOSourceExists(
            @NotNull final RuLDataToRequesterContent source) {

        return new RuLNotificationToRequesterContentDAO(source.getRul());
    }
}
