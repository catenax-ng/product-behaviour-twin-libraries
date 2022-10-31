package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.LoadCollectivesConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawLoadCollectivesRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.LoadCollectives;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputLoadCollectives;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.Instant;

import java.util.List;
import java.util.UUID;


@Component
@EnableTransactionManagement
public class LoadCollectivesTable {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired private RawLoadCollectivesRepository rawLoadCollectivesRepository;
    @Autowired private LoadCollectivesConverter loadCollectivesConverter;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void uploadLoadCollectives(InputLoadCollectives newLoadCollectives) throws OemDatabaseException {
        LoadCollectives collectivesConverted = new LoadCollectives();

        loadCollectivesConverter.convertAndSetId(newLoadCollectives, collectivesConverted,
                                                 UUID.randomUUID().toString());

        this.entityManager.persist(collectivesConverted);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
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
