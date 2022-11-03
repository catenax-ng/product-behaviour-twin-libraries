package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base;

import net.catena_x.btp.libraries.oem.backend.database.util.OemDatabaseException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class RawTableBase<T_Entity> {
    protected abstract EntityManager getEntityManager();

    protected T_Entity refreshAndDetach(T_Entity entity) throws OemDatabaseException {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.refresh(entity);
            entityManager.detach(entity);
        }
        catch(Exception exception) {
            return queryingSingleFailed("Refresh or detach failed!");
        }

        return entity;
    }

    protected List<T_Entity> refreshAndDetach(List<T_Entity> entities) throws OemDatabaseException {
        for (T_Entity entity : entities) {
            refreshAndDetach(entity);
        }
        return entities;
    }

    protected String generateNewId() {
        return UUID.randomUUID().toString();
    }

    protected List<T_Entity> queryingListFailed() throws OemDatabaseException {
        return queryingListFailed("Querying database for entity list failed!");
    }

    protected List<T_Entity> queryingListFailed(String message) throws OemDatabaseException {
        throw new OemDatabaseException(message);
    }

    protected T_Entity queryingSingleFailed() throws OemDatabaseException {
        return queryingSingleFailed("Querying database for entity failed!");
    }

    protected T_Entity queryingSingleFailed(String message) throws OemDatabaseException {
        throw new OemDatabaseException(message);
    }

    protected Optional<T_Entity> queryingOptionalFailed() throws OemDatabaseException {
        return queryingOptionalFailed("Querying database for optional entity failed!");
    }

    protected Optional<T_Entity> queryingOptionalFailed(String message) throws OemDatabaseException {
        throw new OemDatabaseException(message);
    }

    protected long queryingFailed() throws OemDatabaseException {
        return queryingFailed("Querying database failed!");
    }

    protected long queryingFailed(String message) throws OemDatabaseException {
        throw new OemDatabaseException(message);
    }

    protected void executingFailed() throws OemDatabaseException {
        executingFailed("Executing database command failed!");
    }

    protected void executingFailed(String message) throws OemDatabaseException {
        throw new OemDatabaseException(message);
    }
}
