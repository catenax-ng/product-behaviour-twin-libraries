package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawLoadCollectivesRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.LoadCollectives;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputLoadCollective;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableTransactionManagement
public class LoadCollectivesTable {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RawLoadCollectivesRepository rawLoadCollectivesRepository;

    @Transactional
    public void uploadLoadCollectives(InputLoadCollective newLoadCollectives) throws OemDatabaseException {
        LoadCollectives collectives = new LoadCollectives();

        collectives.setId("asdfdasf");
        collectives.setVehicleid(newLoadCollectives.vehicleid());
        collectives.setTimestamp(newLoadCollectives.timestamp());
        collectives.setProductionDate(newLoadCollectives.productionDate());
        collectives.setMileage(newLoadCollectives.mileage());
        collectives.setOperatingseconds(newLoadCollectives.operatingseconds());
        collectives.setLoadcollectives(newLoadCollectives.loadcollective());
        this.entityManager.persist(collectives);
/*
        // TODO the insertion of multiple values with one query can probably be solved more elegantly

        String valuesStr = Arrays.stream(newLoadCollectives.collectives()).map(
                element -> String.format(
                        "(%s, %s, %f, %d, %s)",
                        element.vehicleid(),
                        element.timestamp().toString(),
                        element.mileage(),
                        element.operatingseconds(),
                        element.loadcollective()
                )
        ).collect(Collectors.joining(","));

        Query update = entityManager.createNamedQuery("LoadCollectives.upload").setParameter(1, valuesStr);

        try {
            update.executeUpdate();
        }
        catch(Exception exception) {
            throw new OemDatabaseException("Insert of new load collectives failed!");
        }
        */
    }

    @Transactional
    public List<LoadCollectives> getNewerThan(Instant timestamp) throws OemDatabaseException {
        TypedQuery<LoadCollectives> query = entityManager.createNamedQuery("LoadCollectives.getNewerThan",
                        LoadCollectives.class)
                .setParameter(1, timestamp.toString());

        try {
            return query.getResultList();
        }
        catch(Exception exception) {
            throw new OemDatabaseException("Querying database for load collectives failed!");
        }
    }
}
