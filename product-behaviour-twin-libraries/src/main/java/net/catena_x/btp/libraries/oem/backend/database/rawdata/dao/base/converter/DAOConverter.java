package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DAOConverter<T_DAO, T_DTO> {
    protected abstract T_DTO toDTOSourceExists(T_DAO source);
    protected abstract T_DAO toDAOSourceExists(T_DTO source);

    public T_DTO toDTO(@Nullable final T_DAO source) {
        if(source == null) {
            return null;
        }

        return toDTOSourceExists(source);
    }

    public T_DAO toDAO(@Nullable final T_DTO source) {
        if(source == null) {
            return null;
        }

        return toDAOSourceExists(source);
    }

    public List<T_DTO> toDTO(@Nullable Collection<T_DAO> source) {
        if(source == null) {
            return null;
        }

        final List<T_DTO> convertedList = new ArrayList<>(source.size());
        for (final T_DAO item : source) {
            convertedList.add(toDTO(item));
        }

        return convertedList;
    }

    public List<T_DAO> toDAO(@Nullable final Collection<T_DTO> source) {
        if(source == null) {
            return null;
        }

        final List<T_DAO> convertedList = new ArrayList<>(source.size());
        for (final T_DTO item : source) {
            convertedList.add(toDAO(item));
        }

        return convertedList;
    }
}
