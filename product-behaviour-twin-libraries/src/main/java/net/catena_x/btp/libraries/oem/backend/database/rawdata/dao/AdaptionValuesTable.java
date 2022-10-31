package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.converter.AdaptionValuesConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawAdaptionValuesRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.AdaptionValues;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputAdaptionValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@EnableTransactionManagement
public class AdaptionValuesTable {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private RawAdaptionValuesRepository rawAdaptionValuesRepository;
    @Autowired private AdaptionValuesConverter adaptionValuesConverter;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void uploadAdaptionValues(InputAdaptionValues newAdaptionValues) throws OemDatabaseException {
        AdaptionValues adaptionValuesConverted = new AdaptionValues();

        adaptionValuesConverter.convertAndSetId(newAdaptionValues, adaptionValuesConverted,
                                                UUID.randomUUID().toString());

        this.entityManager.persist(adaptionValuesConverted);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public List<AdaptionValues> getNewerThan(Instant timestamp) throws OemDatabaseException {
        TypedQuery<AdaptionValues> query = entityManager.createNamedQuery("AdaptionValues.getNewerThan",
                        AdaptionValues.class)
                .setParameter(1, timestamp.toString());
        try {
            return query.getResultList();
        }
        catch(Exception exception) {
            throw new OemDatabaseException("Querying database for adaption values failed!");
        }
    }

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
}
