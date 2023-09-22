package net.catena_x.btp.rul.oem.backend.database.rul.tables.vinrelation;

import net.catena_x.btp.libraries.oem.backend.database.util.exceptions.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.Vehicle;
import net.catena_x.btp.libraries.oem.backend.model.dto.vehicle.VehicleTable;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionDefaultCreateNew;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionDefaultUseExisting;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionSerializableCreateNew;
import net.catena_x.btp.rul.oem.backend.database.rul.annotations.RuLTransactionSerializableUseExisting;
import net.catena_x.btp.rul.oem.backend.database.rul.base.RuLTableBase;
import net.catena_x.btp.rul.oem.util.exceptions.OemRuLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Supplier;

@Component
public class RuLVinRelationTableInternal extends RuLTableBase {
    public static final String NO_DATA_REF = "noData";

    @Autowired private RuLVinRelationRepository rulVinRelationRepository;
    @Autowired private VehicleTable vehicleTable;

    private final Logger logger = LoggerFactory.getLogger(RuLVinRelationTableInternal.class);

    @RuLTransactionSerializableUseExisting
    public Exception runSerializableExternalTransaction(@NotNull final Supplier<Exception> function) {
        return function.get();
    }

    @RuLTransactionSerializableCreateNew
    public Exception runSerializableNewTransaction(@NotNull final Supplier<Exception> function) {
        return runSerializableExternalTransaction(function);
    }

    @RuLTransactionDefaultUseExisting
    public void insertExternalTransaction(@NotNull final String vin, @NotNull final String refId,
                                          @NotNull final boolean noData) throws OemRuLException {
        try {
            if(noData) {
                rulVinRelationRepository.insert(vin, NO_DATA_REF, true);
            } else {
                final Vehicle existingVehicle = vehicleTable.getByIdNewTransaction(refId);
                if (!isVehicleRefIdExisting(refId)) {
                    throw new OemRuLException("Vehicle with given ref-id does not exist in rawdata database!");
                }

                rulVinRelationRepository.insert(vin, refId, false);
            }
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Inserting VIN relation failed: " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public void insertNewTransaction(@NotNull final String vin, @NotNull final String refId,
                                     @NotNull final boolean noData) throws OemRuLException {
        insertExternalTransaction(vin, refId, noData);
    }

    private boolean isVehicleRefIdExisting(@NotNull final String refId) throws OemDatabaseException {
        return vehicleTable.getByIdNewTransaction(refId) != null;
    }

    @RuLTransactionDefaultUseExisting
    public void deleteAllExternalTransaction() throws OemRuLException {
        try {
            rulVinRelationRepository.deleteAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting all VIN relations failed: " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public void deleteAllNewTransaction() throws OemRuLException {
        deleteAllExternalTransaction();
    }

    @RuLTransactionSerializableUseExisting
    public void deleteByVinExternalTransaction(@NotNull final String vin) throws OemRuLException {
        try {
            if(!isVinExisting(vin)) {
                throw new OemRuLException("Relation for given vin does not exist!");
            }

            rulVinRelationRepository.deleteByVin(vin);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting VIN relation failed: " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionSerializableCreateNew
    public void deleteByVinNewTransaction(@NotNull final String vin) throws OemRuLException {
        deleteByVinExternalTransaction(vin);
    }

    private boolean isVinExisting(@NotNull final String vin) throws OemRuLException {
        return rulVinRelationRepository.queryByVin(vin) != null;
    }

    @RuLTransactionSerializableUseExisting
    public void deleteByRefIdExternalTransaction(@NotNull final String refId) throws OemRuLException {
        try {
            if(!isRefIdExisting(refId)) {
                throw new OemRuLException("Relation for given ref-id does not exist!");
            }

            rulVinRelationRepository.deleteByRefId(refId);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting VIN relation failed: " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionSerializableCreateNew
    public void deleteByRefIdNewTransaction(@NotNull final String refId) throws OemRuLException {
        deleteByRefIdExternalTransaction(refId);
    }

    @RuLTransactionSerializableUseExisting
    public void deleteByNoDataExternalTransaction(@NotNull final boolean noData) throws OemRuLException {
        try {
            rulVinRelationRepository.deleteByNoData(noData);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Deleting VIN relation failed: " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionSerializableCreateNew
    public void deleteByNoDataNewTransaction(@NotNull final boolean noData) throws OemRuLException {
        deleteByNoDataExternalTransaction(noData);
    }

    private boolean isRefIdExisting(@NotNull final String refId) throws OemRuLException {
        return rulVinRelationRepository.queryByRefId(refId) != null;
    }

    @RuLTransactionDefaultUseExisting
    public List<RuLVinRelationDAO> getAllExternalTransaction() throws OemRuLException {
        try {
            return rulVinRelationRepository.queryAll();
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying VIN relations failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public List<RuLVinRelationDAO> getAllNewTransaction() throws OemRuLException {
        return getAllExternalTransaction();
    }

    @RuLTransactionDefaultUseExisting
    public RuLVinRelationDAO getByVinExternalTransaction(@NotNull final String vin) throws OemRuLException {
        try {
            return rulVinRelationRepository.queryByVin(vin);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying VIN relation failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public RuLVinRelationDAO getByVinNewTransaction(@NotNull final String vin) throws OemRuLException {
        return getByVinExternalTransaction(vin);
    }

    @RuLTransactionDefaultUseExisting
    public RuLVinRelationDAO getByRefIdExternalTransaction(@NotNull final String refId) throws OemRuLException {
        try {
            return rulVinRelationRepository.queryByRefId(refId);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying VIN relation failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public RuLVinRelationDAO getByRefIdNewTransaction(@NotNull final String refId) throws OemRuLException {
        return getByRefIdExternalTransaction(refId);
    }

    @RuLTransactionDefaultUseExisting
    public RuLVinRelationDAO getByNoDataExternalTransaction(@NotNull final boolean noData) throws OemRuLException {
        try {
            return rulVinRelationRepository.queryByNoData(noData);
        } catch (final Exception exception) {
            logger.error(exception.getMessage());
            exception.printStackTrace();
            throw failed("Querying VIN relation failed! " + exception.getMessage(), exception);
        }
    }

    @RuLTransactionDefaultCreateNew
    public RuLVinRelationDAO getByNoDataNewTransaction(@NotNull final boolean noData) throws OemRuLException {
        return getByNoDataExternalTransaction(noData);
    }
}
