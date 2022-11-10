package net.catena_x.btp.libraries.oem.backend.model.dto.sync;

import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter.DAOConverter;
import net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.tables.sync.SyncDAO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public final class SyncConverter extends DAOConverter<SyncDAO, Sync> {
    protected Sync toDTOSourceExists(@NotNull final SyncDAO source) {
        return new Sync(source.getSyncCounter(), source.getQueryTimestamp());
    }

    protected SyncDAO toDAOSourceExists(@NotNull final Sync source) {
        return new SyncDAO(null, source.getSyncCounter(), source.getQueryTimestamp());
    }

    public SyncDAO toDAOWithId(@NotNull String id, @Nullable final Sync source) {
        if(source == null) {
            return null;
        }

        final SyncDAO converted = this.toDAO(source);
        converted.setId(id);
        return converted;
    }
}
