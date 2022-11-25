package net.catena_x.btp.libraries.util.hleper;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;

public final class ContentChecker {
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
}
