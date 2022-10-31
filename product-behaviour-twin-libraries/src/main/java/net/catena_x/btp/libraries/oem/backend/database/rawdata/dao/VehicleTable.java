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
import java.time.Instant;
import java.util.List;

@Component
@EnableTransactionManagement
public class VehicleTable {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private RawVehicleRepository rawVehicleRepository;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void registerVehicle(VehicleInfo newVehicleInfo) throws OemDatabaseException {
        Query register = entityManager.createNamedQuery("Vehicle.register");
        register.setParameter(1, newVehicleInfo.id() );
        register.setParameter(2, newVehicleInfo.van() );
        register.setParameter(3, newVehicleInfo.productionDate() );

        try {
            register.executeUpdate();
        }
        catch(Exception exception) {
            queryingVehiclesFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getVehicles() throws OemDatabaseException {
        try {
            return rawVehicleRepository.findAll();
        }
        catch(Exception exception) {
            return queryingVehiclesFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getVehiclesUpdatedSince(Instant timestamp) throws OemDatabaseException {
        try {
            return rawVehicleRepository.findByUpdateTimestampGreaterThan(timestamp);
        }
        catch(Exception exception) {
            return queryingVehiclesFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<Vehicle> getVehiclesProducedBetween(Instant producedSince, Instant producedUntil)
            throws OemDatabaseException {
        try {
            return rawVehicleRepository.findByProductionDateBetween(producedSince, producedUntil);
        }
        catch(Exception exception) {
            return queryingVehiclesFailed();
        }
    }

    private List<Vehicle> queryingVehiclesFailed() throws OemDatabaseException {
        throw new OemDatabaseException("Querying database for vehicles failed!");
    }
}
