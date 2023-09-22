package net.catena_x.btp.libraries.util.datahelper;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;

public final class DataHelper {
    public static <T> boolean isNullOrEmpty(@Nullable final Collection<T> collection) {
        if(collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    public static <K, V> boolean isNullOrEmpty(@Nullable final HashMap<K, V> collection) {
        if(collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    public static <T> T getFirstAndOnlyItem(@Nullable final Collection<T> collection) throws BtpException {
        if (collection == null) {
            throw new BtpException("Collection was empty!");
        }

        return getFirstAndOnlyItemNotEmpty(collection);
    }

    public static <T> T getFirstAndOnlyItemAllowNull(@Nullable final Collection<T> collection) throws BtpException {
        if (collection == null) {
            return null;
        }

        return getFirstAndOnlyItemNotEmpty(collection);
    }

    public static <T> T getFirstAndOnlyItemNotEmpty(@NotNull final Collection<T> collection) throws BtpException {
        if(collection.size() != 1) {
            throw new BtpException("Collection has more than one item!");
        }

        return collection.iterator().next();
    }

    public static <T> T getFirstItem(@Nullable final Collection<T> collection) throws BtpException {
        if (collection == null) {
            throw new BtpException("Collection was empty!");
        }

        return getFirstItemNotEmpty(collection);
    }

    public static <T> T getFirstItemAllowNull(@Nullable final Collection<T> collection) {
        if (collection == null) {
            return null;
        }

        return getFirstItemNotEmpty(collection);
    }

    public static <T> T getFirstItemNotEmpty(@NotNull final Collection<T> collection) {
        return collection.iterator().next();
    }
}
