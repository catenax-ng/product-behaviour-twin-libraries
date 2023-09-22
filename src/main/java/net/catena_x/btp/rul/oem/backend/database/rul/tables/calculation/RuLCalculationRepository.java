package net.catena_x.btp.rul.oem.backend.database.rul.tables.calculation;

import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public interface RuLCalculationRepository extends Repository<RuLCalculationDAO, String> {
    @Modifying void createNow(@Param("id") @NotNull final String id,
                              @Param("requester_notification_id") @NotNull final String requesterNotificationId,
                              @Param("status") @NotNull final String status,
                              @Param("rul") @Nullable final String rul,
                              @Param("requester_url") @NotNull final String requesterUrl,
                              @Param("requester_bpn") @NotNull final String requesterBpn,
                              @Param("requester_result_asset_id") @NotNull final String requesterResultAssetId);
    @Modifying void insert(@Param("id") @NotNull final String id,
                           @Param("requester_notification_id") @NotNull final String requesterNotificationId,
                           @Param("calculation_timestamp") @NotNull final Instant calculationTimestamp,
                           @Param("status") @NotNull final String status,
                           @Param("rul") @Nullable final String rul,
                           @Param("requester_url") @NotNull final String requesterUrl,
                           @Param("requester_bpn") @NotNull final String requesterBpn,
                           @Param("requester_result_asset_id") @NotNull final String requesterResultAssetId);
    @Modifying void updateStatus(@Param("id") @NotNull final String id, @Param("status") @NotNull final String status);
    @Modifying void updateStatusAndRuL(@Param("id") @NotNull final String id,
                                       @Param("status") @NotNull final String status,
                                       @Param("rul") @NotNull final String rul);
    @Modifying void deleteAll();
    @Modifying void deleteById(@Param("id") @NotNull final String id);
    @Modifying void deleteByRequesterNotificationId(
            @Param("requester_notification_id") @NotNull final String requesterNotificationId);
    @Modifying void deleteByStatus(@Param("status") @NotNull final String status);
    @Modifying void deleteCalculatedUntil(@Param("calculated_until") @NotNull final Instant calculatedUntil);
    List<RuLCalculationDAO> queryAll();
    RuLCalculationDAO queryById(@Param("id") @NotNull final String id);
    RuLCalculationDAO queryByRequesterNotificationId(
            @Param("requester_notification_id") @NotNull final String requesterNotificationId);
    List<RuLCalculationDAO> queryByStatus(@Param("status") @NotNull final String status);
    List<RuLCalculationDAO> queryAllOrderByCalculationTimestamp();
    List<RuLCalculationDAO> queryByCalculationSince(
            @Param("calculation_timestamp_since") @NotNull final Instant calculationTimestampSince);
    List<RuLCalculationDAO> queryByCalculationUntil(
            @Param("calculation_timestamp_until") @NotNull final Instant calculationTimestampUntil);
}
