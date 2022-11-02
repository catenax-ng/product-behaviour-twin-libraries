package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.RawTableBase;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawInfoItemRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Optional;

@Component
@EnableTransactionManagement
public class InfoTable extends RawTableBase<InfoItem> {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private RawInfoItemRepository rawInfoItemRepository;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public void setInfoItem(InfoItem.InfoKey key, String value) throws OemDatabaseException {
        InfoItem infoItem = new InfoItem();
        infoItem.setKey(key);
        infoItem.setValue(value);

        try {
            rawInfoItemRepository.saveAndFlush(infoItem);
        } catch (Exception exception) {
            executingFailed("Inserting info value failed!");
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public InfoItem getInfoItem(InfoItem.InfoKey key) throws OemDatabaseException {
        try {
            Optional<InfoItem> infoItem = rawInfoItemRepository.findById(key);

            if (infoItem.isPresent()) {
                return refreshAndDetach(infoItem.get());
            } else {
                return queryingSingleFailed("Info item is not present!");
            }
        } catch (Exception exception) {
            return queryingSingleFailed("Reading info item failed!");
        }
    }

    public String getInfoValue(InfoItem.InfoKey key) throws OemDatabaseException {
        return getInfoItem(key).getValue();
    }

    public Instant getCurrentDatabaseTimestamp() throws OemDatabaseException {
        return getInfoItem(InfoItem.InfoKey.dataversion).getQueryTimestamp();
    }
}
