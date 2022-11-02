package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.TelemetricsDataConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
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
public class TelemetricsDataTable extends RawTableBase<TelemetricsData> {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private RawTelemetricsDataRepository rawTelemetricsDataRepository;
    @Autowired private TelemetricsDataConverter telemetricsDataConverter;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void flushAndClearCache() {
        this.entityManager.flush();
        this.entityManager.clear();
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void uploadTelemetricsData(InputTelemetricsData newTelemetricsData) throws OemDatabaseException {
        TelemetricsData telemetricsDataConverted = new TelemetricsData();
        telemetricsDataConverter.convertAndSetId(newTelemetricsData, telemetricsDataConverted,
                                                 UUID.randomUUID().toString());

        try {
            rawTelemetricsDataRepository.saveAndFlush(telemetricsDataConverted);
        }
        catch(Exception exception) {
            queryingFailed();
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<TelemetricsData> getUpdatedSince(Instant timestamp) throws OemDatabaseException {
        try {
            return refreshAndDetach(rawTelemetricsDataRepository.findByStorageTimestampGreaterThanEqual(timestamp));
        }
        catch(Exception exception) {
            return queryingListFailed("Querying database for telematics data failed!");
        }
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<TelemetricsData> getAll() throws OemDatabaseException {
        try {
            return refreshAndDetach(rawTelemetricsDataRepository.findAll());
        }
        catch(Exception exception) {
            return queryingListFailed("Querying database for telematics data failed!");
        }
    }
}
