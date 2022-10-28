package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.database.RawInfoItemRepository;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.model.InfoItem;
import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;


@Component
@EnableTransactionManagement
public class InfoTable {
    @Autowired
    private RawInfoItemRepository rawInfoItemRepository;

    public void setInfoItem(InfoItem.InfoKey key, String value) throws OemDatabaseException {
        InfoItem infoItem = new InfoItem();
        infoItem.setKey(key);
        infoItem.setValue(value);

        try {
            rawInfoItemRepository.save(infoItem);
        } catch (Exception exception) {
            throw new OemDatabaseException("Insert/Update of info item failed!");
        }
    }

    public InfoItem getInfoItem(InfoItem.InfoKey key) throws OemDatabaseException {
        try {
            Optional<InfoItem> infoItem = rawInfoItemRepository.findById(key);

            if (infoItem.isPresent()) {
                return infoItem.get();
            } else {
                throw new OemDatabaseException("Info item is not present!");
            }
        } catch (Exception exception) {
            throw new OemDatabaseException("Reading info item failed!");
        }
    }

    public String getInfoValue(InfoItem.InfoKey key) throws OemDatabaseException {
        return getInfoItem(key).getValue();
    }
}
