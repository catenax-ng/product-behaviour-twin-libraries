package net.catena_x.btp.sedc.apps.oem.database.tables.peakloadringbuffer;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.oem.database.annotations.PeakLoadTransactionDefaultCreateNew;
import net.catena_x.btp.sedc.apps.oem.database.annotations.PeakLoadTransactionDefaultUseExisting;
import net.catena_x.btp.sedc.apps.oem.database.annotations.PeakLoadTransactionSerializableCreateNew;
import net.catena_x.btp.sedc.apps.oem.database.annotations.PeakLoadTransactionSerializableUseExisting;
import net.catena_x.btp.sedc.apps.oem.database.base.PeakLoadTableBase;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

@Component
public class PeakLoadRingBufferTableInternal extends PeakLoadTableBase {
    @Autowired private PeakLoadRingBufferRepository peakLoadRingBufferRepository;

    private final Logger logger = LoggerFactory.getLogger(PeakLoadRingBufferTableInternal.class);

    @PeakLoadTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @PeakLoadTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @PeakLoadTransactionSerializableUseExisting
    public void resetDbExternalTransaction() throws BtpException {
        deleteAllExternalTransaction();
    }
    @PeakLoadTransactionSerializableCreateNew
    public void resetDbNewTransaction() throws BtpException {
        resetDbExternalTransaction();
    }

    @PeakLoadTransactionSerializableUseExisting
    public void insertExternalTransaction(
            @NotNull final String id, @NotNull final Instant timestamp, final long dataCollectionPeriodInMs,
            final double recuperation, final double lowTorqueLowRevolutions, final double lowTorqueHighRevolutions,
            final double highTorqueLowRevolutions, final double highTorqueHighRevolutions, final double peakLoad,
            final double averageEnvironmentTemperatureInC, final double stateOfChargeAtStartNormalized,
            final double stateOfChargeAtEndNormalized, @Nullable final Long peakLoadCapability) throws BtpException {
        try {
            peakLoadRingBufferRepository.insert(id, timestamp, dataCollectionPeriodInMs, recuperation,
                    lowTorqueLowRevolutions, lowTorqueHighRevolutions, highTorqueLowRevolutions,
                    highTorqueHighRevolutions, peakLoad, averageEnvironmentTemperatureInC,
                    stateOfChargeAtStartNormalized, stateOfChargeAtEndNormalized, peakLoadCapability);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting peak load values failed! " + exception.getMessage(), exception);
        }
    }

    @PeakLoadTransactionSerializableCreateNew
    public void insertNewTransaction(
            @NotNull final String id, @NotNull final Instant timestamp, final long dataCollectionPeriodInMs,
            final double recuperation, final double lowTorqueLowRevolutions, final double lowTorqueHighRevolutions,
            final double highTorqueLowRevolutions, final double highTorqueHighRevolutions, final double peakLoad,
            final double averageEnvironmentTemperatureInC, final double stateOfChargeAtStartNormalized,
            final double stateOfChargeAtEndNormalized, @Nullable final Long peakLoadCapability) throws BtpException {
        insertExternalTransaction(id, timestamp, dataCollectionPeriodInMs, recuperation, lowTorqueLowRevolutions,
                lowTorqueHighRevolutions, highTorqueLowRevolutions, highTorqueHighRevolutions, peakLoad,
                averageEnvironmentTemperatureInC, stateOfChargeAtStartNormalized, stateOfChargeAtEndNormalized,
                peakLoadCapability);
    }

    @PeakLoadTransactionSerializableUseExisting
    public void insertAndRemoveExternalTransaction(
            @NotNull final String id, @NotNull final Instant timestamp, final long dataCollectionPeriodInMs,
            final double recuperation, final double lowTorqueLowRevolutions, final double lowTorqueHighRevolutions,
            final double highTorqueLowRevolutions, final double highTorqueHighRevolutions, final double peakLoad,
            final double averageEnvironmentTemperatureInC, final double stateOfChargeAtStartNormalized,
            final double stateOfChargeAtEndNormalized, @Nullable final Long peakLoadCapability,
            @NotNull final String removeId) throws BtpException {
        insertExternalTransaction(id, timestamp, dataCollectionPeriodInMs, recuperation, lowTorqueLowRevolutions,
                lowTorqueHighRevolutions, highTorqueLowRevolutions, highTorqueHighRevolutions, peakLoad,
                averageEnvironmentTemperatureInC, stateOfChargeAtStartNormalized, stateOfChargeAtEndNormalized,
                peakLoadCapability);
        deleteByIdExternalTransaction(removeId);
    }

    @PeakLoadTransactionSerializableCreateNew
    public void insertAndRemoveNewTransaction(
            @NotNull final String id, @NotNull final Instant timestamp, final long dataCollectionPeriodInMs,
            final double recuperation, final double lowTorqueLowRevolutions, final double lowTorqueHighRevolutions,
            final double highTorqueLowRevolutions, final double highTorqueHighRevolutions, final double peakLoad,
            final double averageEnvironmentTemperatureInC, final double stateOfChargeAtStartNormalized,
            final double stateOfChargeAtEndNormalized, @Nullable final Long peakLoadCapability,
            @NotNull final String removeId) throws BtpException {
        insertAndRemoveExternalTransaction(id, timestamp, dataCollectionPeriodInMs, recuperation, lowTorqueLowRevolutions,
                lowTorqueHighRevolutions, highTorqueLowRevolutions, highTorqueHighRevolutions, peakLoad,
                averageEnvironmentTemperatureInC, stateOfChargeAtStartNormalized, stateOfChargeAtEndNormalized,
                peakLoadCapability, removeId);
    }

    @PeakLoadTransactionSerializableUseExisting
    public void setPeakLoadCapabilityExternalTransaction(@NotNull final String id, final long peakLoadCapability)
            throws BtpException {
        try {
            peakLoadRingBufferRepository.setPeakLoadCapability(id, peakLoadCapability);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Setting peak load capability failed! " + exception.getMessage(), exception);
        }
    }

    @PeakLoadTransactionSerializableCreateNew
    public void setPeakLoadCapabilityNewTransaction(@NotNull final String id, final long peakLoadCapability)
            throws BtpException {
        setPeakLoadCapabilityExternalTransaction(id, peakLoadCapability);
    }

    @PeakLoadTransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws BtpException {
        try {
            peakLoadRingBufferRepository.deleteAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all peak load values failed! " + exception.getMessage(), exception);
        }
    }

    @PeakLoadTransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws BtpException {
        deleteAllExternalTransaction();
    }

    @PeakLoadTransactionSerializableUseExisting
    public void deleteByIdExternalTransaction(@NotNull final String id) throws BtpException {
        try {
            peakLoadRingBufferRepository.deleteById(id);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting peak load values failed! " + exception.getMessage(), exception);
        }
    }

    @PeakLoadTransactionSerializableCreateNew
    public void deleteByIdNewTransaction(@NotNull final String id) throws BtpException {
        deleteByIdExternalTransaction(id);
    }

    @PeakLoadTransactionSerializableUseExisting
    public List<PeakLoadRingBufferElementDAO> getAllSortByTimestampExternalTransaction() throws BtpException {
        try {
            return peakLoadRingBufferRepository.queryAllSortByTimestamp();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying peak load values failed! " + exception.getMessage(), exception);
        }
    }

    @PeakLoadTransactionSerializableCreateNew
    public List<PeakLoadRingBufferElementDAO> getAllSortByTimestampNewTransaction() throws BtpException {
        return getAllSortByTimestampExternalTransaction();
    }

    @PeakLoadTransactionSerializableUseExisting
    public PeakLoadRingBufferElementDAO getByIdExternalTransaction(@NotNull final String id) throws BtpException {
        try {
            return peakLoadRingBufferRepository.queryById(id);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying peak load values by id failed! " + exception.getMessage(), exception);
        }
    }

    @PeakLoadTransactionSerializableCreateNew
    public PeakLoadRingBufferElementDAO getByIdNewTransaction(@NotNull final String id) throws BtpException {
        return getByIdExternalTransaction(id);
    }
}
