package net.catena_x.btp.sedc.apps.oem.database.dto.peakloadringbuffer;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.apps.oem.database.tables.peakloadringbuffer.PeakLoadRingBufferElementDAO;
import net.catena_x.btp.sedc.apps.oem.database.tables.peakloadringbuffer.PeakLoadRingBufferTableInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class PeakLoadRingBufferTable {
    @Autowired private PeakLoadRingBufferTableInternal internal;
    @Autowired private PeakLoadRingBufferConverter peakLoadRingBufferConverter;

    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableNewTransaction(function);
    }

    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return internal.runSerializableExternalTransaction(function);
    }

    public void resetDbNewTransaction() throws BtpException {
        internal.resetDbNewTransaction();
    }

    public void resetDbExternalTransaction() throws BtpException {
        internal.resetDbExternalTransaction();
    }

    public void insertNewTransaction(@NotNull final PeakLoadRingBufferElement ringBuffer)
            throws BtpException {
        final PeakLoadRingBufferElementDAO ringBufferDAO = peakLoadRingBufferConverter.toDAOSourceExists(ringBuffer);
        internal.insertNewTransaction(ringBufferDAO.getId(), ringBufferDAO.getTimestamp(),
                ringBufferDAO.getDataCollectionPeriodInMs(), ringBufferDAO.getRecuperation(),
                ringBufferDAO.getLowTorqueLowRevolutions(), ringBufferDAO.getLowTorqueHighRevolutions(),
                ringBufferDAO.getHighTorqueLowRevolutions(), ringBufferDAO.getHighTorqueHighRevolutions(),
                ringBufferDAO.getPeakLoad(), ringBufferDAO.getAverageEnvironmentTemperatureInC(),
                ringBufferDAO.getStateOfChargeAtStartNormalized(), ringBufferDAO.getStateOfChargeAtEndNormalized(),
                ringBufferDAO.getPeakLoadCapability());
    }

    public void insertExternalTransaction(@NotNull final PeakLoadRingBufferElement ringBuffer) throws BtpException {
        final PeakLoadRingBufferElementDAO ringBufferDAO = peakLoadRingBufferConverter.toDAOSourceExists(ringBuffer);
        internal.insertExternalTransaction(ringBufferDAO.getId(), ringBufferDAO.getTimestamp(),
                ringBufferDAO.getDataCollectionPeriodInMs(), ringBufferDAO.getRecuperation(),
                ringBufferDAO.getLowTorqueLowRevolutions(), ringBufferDAO.getLowTorqueHighRevolutions(),
                ringBufferDAO.getHighTorqueLowRevolutions(), ringBufferDAO.getHighTorqueHighRevolutions(),
                ringBufferDAO.getPeakLoad(), ringBufferDAO.getAverageEnvironmentTemperatureInC(),
                ringBufferDAO.getStateOfChargeAtStartNormalized(), ringBufferDAO.getStateOfChargeAtEndNormalized(),
                ringBufferDAO.getPeakLoadCapability());
    }

    public void insertAndRemoveNewTransaction(@NotNull final PeakLoadRingBufferElement ringBuffer,
                                              @NotNull final String removeId) throws BtpException {
        final PeakLoadRingBufferElementDAO ringBufferDAO = peakLoadRingBufferConverter.toDAOSourceExists(ringBuffer);
        internal.insertAndRemoveNewTransaction(ringBufferDAO.getId(), ringBufferDAO.getTimestamp(),
                ringBufferDAO.getDataCollectionPeriodInMs(), ringBufferDAO.getRecuperation(),
                ringBufferDAO.getLowTorqueLowRevolutions(), ringBufferDAO.getLowTorqueHighRevolutions(),
                ringBufferDAO.getHighTorqueLowRevolutions(), ringBufferDAO.getHighTorqueHighRevolutions(),
                ringBufferDAO.getPeakLoad(), ringBufferDAO.getAverageEnvironmentTemperatureInC(),
                ringBufferDAO.getStateOfChargeAtStartNormalized(), ringBufferDAO.getStateOfChargeAtEndNormalized(),
                ringBufferDAO.getPeakLoadCapability(), removeId);
    }

    public void insertAndRemoveExternalTransaction(@NotNull final PeakLoadRingBufferElement ringBuffer,
                                                   @NotNull final String removeId) throws BtpException {

        final PeakLoadRingBufferElementDAO ringBufferDAO = peakLoadRingBufferConverter.toDAOSourceExists(ringBuffer);
        internal.insertAndRemoveExternalTransaction(ringBufferDAO.getId(), ringBufferDAO.getTimestamp(),
                ringBufferDAO.getDataCollectionPeriodInMs(), ringBufferDAO.getRecuperation(),
                ringBufferDAO.getLowTorqueLowRevolutions(), ringBufferDAO.getLowTorqueHighRevolutions(),
                ringBufferDAO.getHighTorqueLowRevolutions(), ringBufferDAO.getHighTorqueHighRevolutions(),
                ringBufferDAO.getPeakLoad(), ringBufferDAO.getAverageEnvironmentTemperatureInC(),
                ringBufferDAO.getStateOfChargeAtStartNormalized(), ringBufferDAO.getStateOfChargeAtEndNormalized(),
                ringBufferDAO.getPeakLoadCapability(), removeId);
    }

    public void setPeakLoadCapabilityNewTransaction(@NotNull final String id, long peakLoadCapability)
            throws BtpException {
        internal.setPeakLoadCapabilityNewTransaction(id, peakLoadCapability);
    }

    public void setPeakLoadCapabilityExternalTransaction(@NotNull final String id, long peakLoadCapability)
            throws BtpException {
        internal.setPeakLoadCapabilityExternalTransaction(id, peakLoadCapability);
    }

    public void deleteAllNewTransaction() throws BtpException {
        internal.deleteAllNewTransaction();
    }

    public void deleteAllExternalTransaction() throws BtpException {
        internal.deleteAllExternalTransaction();
    }

    public void deleteByIdNewTransaction(@NotNull final String id) throws BtpException {
        internal.deleteByIdNewTransaction(id);
    }

    public void deleteByIdExternalTransaction(@NotNull final String id) throws BtpException {
        internal.deleteByIdExternalTransaction(id);
    }

    public PeakLoadRingBufferElement getByIdNewTransaction(@NotNull final String id) throws BtpException {
        return peakLoadRingBufferConverter.toDTO(internal.getByIdNewTransaction(id));
    }

    public PeakLoadRingBufferElement getByIdExternalTransaction(@NotNull final String id) throws BtpException {
        return peakLoadRingBufferConverter.toDTO(internal.getByIdExternalTransaction(id));
    }

    public List<PeakLoadRingBufferElement> getAllSortByTimestampNewTransaction()
            throws BtpException {
        return peakLoadRingBufferConverter.toDTO(internal.getAllSortByTimestampNewTransaction());
    }

    public List<PeakLoadRingBufferElement> getAllSortByTimestampExternalTransaction()
            throws BtpException {
        return peakLoadRingBufferConverter.toDTO(internal.getAllSortByTimestampExternalTransaction());
    }
}