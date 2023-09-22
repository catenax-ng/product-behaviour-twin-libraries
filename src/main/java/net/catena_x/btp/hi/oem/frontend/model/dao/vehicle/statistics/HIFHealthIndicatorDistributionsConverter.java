package net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics;

import net.catena_x.btp.hi.oem.frontend.model.dto.statistics.HIHealthIndicatorDistributions;
import net.catena_x.btp.libraries.util.database.converter.DAOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.io.UncheckedIOException;

@Component
public class HIFHealthIndicatorDistributionsConverter
        extends DAOConverter<HIFHealthIndicatorDistributions, HIHealthIndicatorDistributions> {

    @Autowired HIFHistogramConverter hifHistogramConverter;

    protected HIHealthIndicatorDistributions toDTOSourceExists(@NotNull final HIFHealthIndicatorDistributions source) {
        throw new UncheckedIOException("Conversion is not allowed!", null);
    }

    protected HIFHealthIndicatorDistributions toDAOSourceExists(@NotNull final HIHealthIndicatorDistributions source) {
        return new HIFHealthIndicatorDistributions(
                hifHistogramConverter.toDAO(source.getHistogramLoadSpectra()),
                hifHistogramConverter.toDAO(source.getHistogramAdaptionValues()));
    }
}
