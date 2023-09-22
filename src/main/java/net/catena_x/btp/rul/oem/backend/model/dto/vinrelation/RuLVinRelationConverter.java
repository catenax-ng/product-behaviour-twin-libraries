package net.catena_x.btp.rul.oem.backend.model.dto.vinrelation;

import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation.RuLVinRelationDAO;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public final class RuLVinRelationConverter extends DAOConverter<RuLVinRelationDAO, RuLVinRelation> {
    protected RuLVinRelation toDTOSourceExists(@NotNull final RuLVinRelationDAO source) {
        return new RuLVinRelation(source.getVin(), source.getRefId(), source.isNoData());
    }

    protected RuLVinRelationDAO toDAOSourceExists(@NotNull final RuLVinRelation source) {
        return new RuLVinRelationDAO(source.getVin(), source.getRefId(), source.isNoData());
    }
}
