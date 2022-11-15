package net.catena_x.btp.libraries.oem.backend.database.rawdata.dao.base.converter;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

        return source.stream().map(item -> toDTO(item)).collect(Collectors.toList());
    }

    public List<T_DAO> toDAO(@Nullable final Collection<T_DTO> source) {
        if(source == null) {
            return null;
        }

        return source.stream().map(item -> toDAO(item)).collect(Collectors.toList());
    }
}
