package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.TelemetricsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.VehicleConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.base.TelemetricsRawInputSource;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawVehicleRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
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

import javax.persistence.*;
import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void flushAndClearCache() {
        this.entityManager.flush();
        this.entityManager.clear();
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void registerVehicle(VehicleInfo newVehicleInfo) throws OemDatabaseException {
        Vehicle vehicleConverted = new Vehicle();
        vehicleConverter.convert(newVehicleInfo, vehicleConverted);

        try {
            rawVehicleRepository.saveAndFlush(vehicleConverted);
        }
        catch(Exception exception) {
            queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void appendTelematicsDataById(String id, InputTelemetricsData newTelemetricsData)
            throws OemDatabaseException {
        Vehicle vehicle = findById(id);
        appendTelematicsData(vehicle, newTelemetricsData);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void appendTelematicsDataByVan(String van, InputTelemetricsData newTelemetricsData)
            throws OemDatabaseException {
        Vehicle vehicle = findById(van);
        appendTelematicsData(vehicle, newTelemetricsData);
    }

    private void appendTelematicsData(Vehicle vehicle, InputTelemetricsData newTelemetricsData)
            throws OemDatabaseException {

        this.entityManager.detach(vehicle);

        if( telemetricsDataIsNewer(newTelemetricsData, vehicle.getTelemetricsData() ) ) {
            TelemetricsData telemetricsDataConverted = new TelemetricsData();

            telemetricsDataConverter.convertAndSetId(newTelemetricsData, telemetricsDataConverted,
                                                     UUID.randomUUID().toString());

            vehicle.setTelemetricsData(telemetricsDataConverted);
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

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void deleteById(String id) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteById(id);
        }
        catch(Exception exception) {
            executingFailed("Deleting vehicle by id failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void deleteByVan(String van) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVan(van);
        }
        catch(Exception exception) {
            executingFailed("Deleting vehicle by van failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public Vehicle findById(String id) throws OemDatabaseException {
        try {
            Optional<Vehicle> vehicle = rawVehicleRepository.findById(id);

            if (vehicle.isPresent()) {
                return refreshAndDetach(vehicle.get());
            } else {
                return queryingSingleFailed("Querying vehicle by id failed, vehicle not present!");
            }
        } catch (Exception exception) {
            return queryingSingleFailed("Querying vehicle by id failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public Vehicle findByVan(String van) throws OemDatabaseException {
        try {
            Optional<Vehicle> vehicle = rawVehicleRepository.findByVan(van);

            if (vehicle.isPresent()) {
                return refreshAndDetach(vehicle.get());
            } else {
                return queryingSingleFailed("Querying vehicle by van failed, vehicle not present!");
            }
        } catch (Exception exception) {
            return queryingSingleFailed("Querying vehicle by van failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getAll() throws OemDatabaseException {
        try {
            return refreshAndDetach(rawVehicleRepository.findAll());
        }
        catch(Exception exception) {
            return queryingListFailed("Querying vehicles failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getUpdatedSince(Instant timestamp) throws OemDatabaseException {
        try {
            return refreshAndDetach(rawVehicleRepository.findByUpdateTimestampGreaterThan(timestamp));
        }
        catch(Exception exception) {
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
            return queryingListFailed("Querying vehicles failed!");
        }
    }
}
