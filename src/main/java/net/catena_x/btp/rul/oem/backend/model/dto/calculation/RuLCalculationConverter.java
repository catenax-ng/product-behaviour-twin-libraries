package net.catena_x.btp.rul.oem.backend.model.dto.calculation;

import net.catena_x.btp.libraries.edc.model.EdcAssetAddress;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.database.rul.tables.calculation.RuLCalculationDAO;
import net.catena_x.btp.rul.oem.backend.model.dto.util.RuLRemainingUsefulLifeConverter;
import net.catena_x.btp.rul.oem.backend.model.enums.RuLCalculationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class RuLCalculationConverter extends DAOConverter<RuLCalculationDAO, RuLCalculation> {
    @Autowired private RuLRemainingUsefulLifeConverter rulRemainingUsefulLifeConverter;

    protected RuLCalculation toDTOSourceExists(@NotNull final RuLCalculationDAO source) {
        return new RuLCalculation(source.getId(), source.getRequesterNotificationId(),
                                  source.getCalculationTimestamp(), RuLCalculationStatus.valueOf(source.getStatus()),
                                  rulRemainingUsefulLifeConverter.toDTO(source.getRul()),
                                  new EdcAssetAddress(source.getRequesterUrl(), source.getRequesterBpn(),
                                                      source.getRequesterResultAssetId()));
    }

    protected RuLCalculationDAO toDAOSourceExists(@NotNull final RuLCalculation source) {
        return new RuLCalculationDAO(source.getId(), source.getRequesterNotificationId(),
                source.getCalculationTimestamp(), source.getStatus().toString(),
                rulRemainingUsefulLifeConverter.toDAO(source.getRul()),
                source.getRequesterResultAddress().getConnectorUrl(), source.getRequesterResultAddress().getBpn(),
                source.getRequesterResultAddress().getAssetId());
    }
}
