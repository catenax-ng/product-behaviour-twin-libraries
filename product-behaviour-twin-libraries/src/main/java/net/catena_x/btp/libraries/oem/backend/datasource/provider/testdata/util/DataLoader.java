package net.catena_x.btp.libraries.oem.backend.datasource.provider.testdata.util;

import net.catena_x.btp.libraries.oem.backend.datasource.provider.util.exceptions.DataProviderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class DataLoader {
    protected <T> boolean isNullOrEmpty(@Nullable final Collection<T> collection) {
        if(collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    protected <T> T getFirstAndOnlyItem(@Nullable final Collection<T> collection) throws DataProviderException {
        if (collection == null) {
            throw new DataProviderException("Collection was empty!");
        }

        return getFirstAndOnlyItemNotEmpty(collection);
    }

    protected <T> T getFirstAndOnlyItemAllowNull(@Nullable final Collection<T> collection)
            throws DataProviderException {
        if (collection == null) {
            return null;
        }

        return getFirstAndOnlyItemNotEmpty(collection);
    }

    private <T> T getFirstAndOnlyItemNotEmpty(@NotNull final Collection<T> collection) throws DataProviderException {
        if(collection.size() != 1) {
            throw new DataProviderException("Collection has more than one item!");
        }

        return collection.iterator().next();
    }
}
