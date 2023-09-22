package net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics;

import net.catena_x.btp.hi.oem.frontend.model.dto.statistics.HIHistogram;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.UncheckedIOException;

@Component
public class HIFHistogramConverter extends DAOConverter<HIFHistogram, HIHistogram> {
    protected HIHistogram toDTOSourceExists(@NotNull final HIFHistogram source) {
        throw new UncheckedIOException("Conversion is not allowed!", null);
    }

    protected HIFHistogram toDAOSourceExists(@NotNull final HIHistogram source) {
        return new HIFHistogram(source.countGreen(), source.countYellow(), source.countRed(), source.countUnknown());
    }
}
