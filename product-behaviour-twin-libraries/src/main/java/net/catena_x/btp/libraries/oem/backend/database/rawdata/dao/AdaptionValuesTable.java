package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawAdaptionValuesRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.AdaptionValues;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import net.catena_x.btp.libraries.oem.backend.datasource.model.rawdata.InputAdaptionValues;
import net.catena_x.btp.libraries.oem.backend.util.SQLFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableTransactionManagement
public class AdaptionValuesTable {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RawAdaptionValuesRepository rawAdaptionValuesRepository;

    @Transactional
    public void uploadAdaptionValues(InputAdaptionValues newAdaptionValues) throws OemDatabaseException {
/*

        // TODO the insertion of multiple values with one query can probably be solved more elegantly
        String valuesStr = Arrays.stream(newAdaptionValues.adaptionValues()).map(
                element -> String.format(
                        "(%s, %s, %f, %d, %s)",
                        element.vehicleid(),
                        element.timestamp().toString(),
                        element.mileage(),
                        element.operatingseconds(),
                        SQLFormatter.doubleArrayToSQLArrayStr(element.adaptionvalues())
                    )
        ).collect(Collectors.joining(","));

        Query upload = entityManager.createNamedQuery("AdaptionValues.upload").setParameter(1, valuesStr);

        try {
            upload.executeUpdate();
        }
        catch(Exception exception) {
            throw new OemDatabaseException("Insert of new adaption values failed!");
        }

 */
    }

    @Transactional
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
}
