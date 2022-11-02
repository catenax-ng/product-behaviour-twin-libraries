package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.TelemetricsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawTelemetricsDataRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.TelemetricsData;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputTelemetricsData;
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
public class TelemetricsDataTable {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private RawTelemetricsDataRepository rawTelemetricsDataRepository;
    @Autowired private TelemetricsDataConverter loadCollectivesConverter;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void uploadTelemetricsData(InputTelemetricsData newTelemetricsData) throws OemDatabaseException {
        TelemetricsData telemetricsDataConverted = new TelemetricsData();
        loadCollectivesConverter.convertAndSetId(newTelemetricsData, telemetricsDataConverted,
                                                 UUID.randomUUID().toString());

        this.entityManager.persist(telemetricsDataConverted);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<TelemetricsData> getNewerThan(Instant timestamp) throws OemDatabaseException {
        TypedQuery<TelemetricsData> query = entityManager.createNamedQuery("LoadCollectives.getNewerThan",
                        TelemetricsData.class)
                .setParameter(1, timestamp.toString());

        try {
            return query.getResultList();
        }
        catch(Exception exception) {
            throw new OemDatabaseException("Querying database for load collectives failed!");
        }
    }

    /*
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<AdaptionValues> getAdaptionValues() throws OemDatabaseException {
        try {
            return rawAdaptionValuesRepository.findAll();
        }
        catch(Exception exception) {
            return queryingAdaptionValuesFailed();
        }
    }

    private List<AdaptionValues> queryingAdaptionValuesFailed() throws OemDatabaseException {
        throw new OemDatabaseException("Querying database for adaption values failed!");
    }
    */
}
