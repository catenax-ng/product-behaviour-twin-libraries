package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.TelemetricsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.VehicleConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsRawInputSource;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawVehicleRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelemetricsData;
import net.catena_x.btp.libraries.oem.backend.datasource.model.registration.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@EnableTransactionManagement
public class VehicleTable extends RawTableBase<Vehicle> {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private RawVehicleRepository rawVehicleRepository;
    @Autowired private TelemetricsDataConverter telemetricsDataConverter;
    @Autowired private VehicleConverter vehicleConverter;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void registerVehicle(VehicleInfo newVehicleInfo) throws OemDatabaseException {
        saveVehicle(vehicleConverter.convert(newVehicleInfo));
    }

    private void saveVehicle(Vehicle vehicle) throws OemDatabaseException {
        try {
            this.entityManager.persist(vehicle);
            this.entityManager.flush();
        }
        catch(Exception exception) {
            this.entityManager.clear();
            executingFailed("Failed to register vehicle!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void appendTelematicsDataById(String id, InputTelemetricsData newTelemetricsData)
            throws OemDatabaseException {
        appendTelematicsData(findById(id), newTelemetricsData);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void appendTelematicsDataByVan(String van, InputTelemetricsData newTelemetricsData)
            throws OemDatabaseException {
        appendTelematicsData(findByVan(van), newTelemetricsData);
    }

    private void appendTelematicsData(Vehicle vehicle, InputTelemetricsData newTelemetricsData)
            throws OemDatabaseException {

        if( telemetricsDataIsNewer(newTelemetricsData, vehicle.getTelemetricsData()) ) {
            applyNewTelemetricsData(vehicle, newTelemetricsData);
            rawVehicleRepository.saveAndFlush(vehicle);
        }
    }

    private boolean telemetricsDataIsNewer(TelemetricsRawInputSource newTelemetricsData,
                                           TelemetricsData telemetricsData) {
        if(telemetricsData == null) {
            return true;
        }

        return newTelemetricsData.state().creationTimestamp().isAfter(telemetricsData.getCreationTimestamp());
    }

    private void applyNewTelemetricsData(Vehicle vehicle, InputTelemetricsData newTelemetricsData) {
        vehicle.setTelemetricsData(telemetricsDataConverter.convertAndSetId(newTelemetricsData, generateNewId()));
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void deleteById(String id) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteById(id);
        }
        catch(Exception exception) {
            this.entityManager.clear();
            executingFailed("Deleting vehicle by id failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void deleteByVan(String van) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVan(van);
        }
        catch(Exception exception) {
            this.entityManager.clear();
            executingFailed("Deleting vehicle by van failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public Vehicle findById(String id) throws OemDatabaseException {
        Optional<Vehicle> vehicle = null;

        try {
            vehicle = rawVehicleRepository.findById(id);
        } catch (Exception exception) {
            return queryingSingleFailed("Querying vehicle by id failed!");
        }

        return unwrap(vehicle);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public Vehicle findByVan(String van) throws OemDatabaseException {
        Optional<Vehicle> vehicle = null;

        try {
            vehicle = rawVehicleRepository.findByVan(van);
        } catch (Exception exception) {
            return queryingSingleFailed("Querying vehicle by van failed!");
        }

        return unwrap(vehicle);
    }

    private Vehicle unwrap(Optional<Vehicle> vehicle) throws OemDatabaseException {
        if (vehicle.isPresent()) {
            return refreshAndDetach(vehicle.get());
        } else {
            return queryingSingleFailed("Querying vehicle failed, vehicle not present!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getAll() throws OemDatabaseException {
        try {
            return refreshAndDetach(rawVehicleRepository.findAll());
        }
        catch(Exception exception) {
            this.entityManager.clear();
            return queryingListFailed("Querying vehicles failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getUpdatedSince(Instant timestamp) throws OemDatabaseException {
        try {
            return refreshAndDetach(rawVehicleRepository.findByUpdateTimestampGreaterThan(timestamp));
        }
        catch(Exception exception) {
            this.entityManager.clear();
            return queryingListFailed("Querying vehicles failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getProducedBetween(Instant producedSince, Instant producedUntil)
            throws OemDatabaseException {
        try {
            return refreshAndDetach(rawVehicleRepository.findByProductionDateBetween(producedSince, producedUntil));
        }
        catch(Exception exception) {
            this.entityManager.clear();
            return queryingListFailed("Querying vehicles failed!");
        }
    }
}
