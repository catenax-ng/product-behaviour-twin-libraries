package net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface RuLVinRelationRepository extends Repository<RuLVinRelationDAO, String> {
    @Modifying void insert(@Param("vin") @NotNull final String vin, @Param("ref_id") @NotNull final String refId,
            @Param("no_data") @NotNull final boolean noData);
    @Modifying void deleteAll();
    @Modifying void deleteByVin(@Param("vin") @NotNull final String vin);
    @Modifying void deleteByRefId(@Param("ref_id") @NotNull final String refId);
    @Modifying void deleteByNoData(@Param("no_data") @NotNull final boolean noData);
    List<RuLVinRelationDAO> queryAll();
    RuLVinRelationDAO queryByVin(@Param("vin") @NotNull final String vin);
    RuLVinRelationDAO queryByRefId(@Param("ref_id") @NotNull final String refId);
    RuLVinRelationDAO queryByNoData(@Param("no_data") @NotNull final boolean noData);
}
