package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.telematicsdata;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public interface TelematicsDataRepository extends Repository<TelematicsDataDAO, String> {
    @Modifying void insert(@Param("id") @NotNull final String id,
                           @Param("vehicle_id") @NotNull final String vehicleId,
                           @Param("load_spectra") @NotNull final String loadSpectra,
                           @Param("adaption_values") @NotNull final String adaptionValues,
                           @Param("sync_counter") @NotNull final long syncCounter);
    @Modifying void deleteAll();
    @Modifying void deleteById(@Param("id") @NotNull final String id);
    @Modifying void deleteByVehicleId(@Param("vehicle_id") @NotNull final String vehicleId);
    @Modifying void deleteStoredUntil(@Param("stored_until") @NotNull final Instant storedUntil);
    @Modifying void deleteSyncCounterUntil(@Param("sync_counter") @NotNull final long syncCounter);
    List<TelematicsDataDAO> queryAll();
    TelematicsDataDAO queryById(@Param("id") @NotNull final String id);
    List<TelematicsDataDAO> queryAllOrderByStorageTimestamp();
    List<TelematicsDataDAO> queryAllOrderBySyncCounter();
    List<TelematicsDataDAO> queryByVehicleId(@Param("vehicle_id") @NotNull final String vehicleId);
    List<TelematicsDataDAO> queryByStorageSince(
            @Param("storage_timestamp_since") @NotNull final Instant storageTimestampSince);
    List<TelematicsDataDAO> queryByStorageUntil(
            @Param("storage_timestamp_until") @NotNull final Instant storageTimestampUntil);
    List<TelematicsDataDAO> queryBySyncCounterSince(@Param("sync_counter_since") @NotNull final long syncCounterSince);
    List<TelematicsDataDAO> queryBySyncCounterUntil(@Param("sync_counter_until") @NotNull final long syncCounterUntil);
}
