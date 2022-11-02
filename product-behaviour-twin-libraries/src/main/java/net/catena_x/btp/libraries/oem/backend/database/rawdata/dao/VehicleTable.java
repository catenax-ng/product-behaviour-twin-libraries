package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawVehicleRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.Vehicle;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
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

@Component
@EnableTransactionManagement
public class VehicleTable {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private RawVehicleRepository rawVehicleRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void flushAndClearCache() {
        this.entityManager.flush();
        this.entityManager.clear();
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void registerVehicle(VehicleInfo newVehicleInfo) throws OemDatabaseException {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(newVehicleInfo.id());
        vehicle.setVan(newVehicleInfo.van());
        vehicle.setProductionDate(newVehicleInfo.productionDate());

        try {
            this.entityManager.persist(vehicle);
            this.entityManager.flush();
            this.entityManager.refresh(vehicle);
        }
        catch(Exception exception) {
            queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void deleteById(String id) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteById(id);
        }
        catch(Exception exception) {
            queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void deleteByVan(String van) throws OemDatabaseException {
        try {
            rawVehicleRepository.deleteByVan(van);
        }
        catch(Exception exception) {
            queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public Optional<Vehicle> findById(String id) throws OemDatabaseException {
        try {
            return rawVehicleRepository.findById(id);
        }
        catch(Exception exception) {
            queryingFailed();
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public Optional<Vehicle> findByVan(String van) throws OemDatabaseException {
        try {
            return rawVehicleRepository.findByVan(van);
        }
        catch(Exception exception) {
            queryingFailed();
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getAll() throws OemDatabaseException {
        try {
            return rawVehicleRepository.findAll();
        }
        catch(Exception exception) {
            return queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getAllDetached() throws OemDatabaseException {
        try {
            List<Vehicle> vehicles = rawVehicleRepository.findAll();
            for (Vehicle vehicle:vehicles) {
                this.entityManager.detach(vehicle);
            }
            return vehicles;
        }
        catch(Exception exception) {
            return queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getUpdatedSince(Instant timestamp) throws OemDatabaseException {
        try {
            this.entityManager.clear();
            return rawVehicleRepository.findByUpdateTimestampGreaterThan(timestamp);
        }
        catch(Exception exception) {
            return queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getProducedBetween(Instant producedSince, Instant producedUntil)
            throws OemDatabaseException {
        try {
            return rawVehicleRepository.findByProductionDateBetween(producedSince, producedUntil);
        }
        catch(Exception exception) {
            return queryingFailed();
        }
    }

    private List<Vehicle> queryingFailed() throws OemDatabaseException {
        throw new OemDatabaseException("Querying database for vehicles failed!");
    }
}
