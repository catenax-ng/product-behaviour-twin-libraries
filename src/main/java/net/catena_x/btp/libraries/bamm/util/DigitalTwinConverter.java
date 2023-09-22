package net.catena_x.btp.libraries.bamm.util;

import net.catena_x.btp.libraries.bamm.common.BammLoaddataSource;
import net.catena_x.btp.libraries.bamm.common.BammStatus;
import net.catena_x.btp.libraries.bamm.custom.adaptionvalues.AdaptionValues;
import net.catena_x.btp.libraries.bamm.custom.classifiedloadspectrum.ClassifiedLoadSpectrum;
import net.catena_x.btp.libraries.bamm.custom.damage.Damage;
import net.catena_x.btp.libraries.bamm.custom.remainingusefullife.RemainingUsefulLife;
import net.catena_x.btp.libraries.bamm.digitaltwin.DigitalTwin;
import net.catena_x.btp.libraries.util.exceptions.BtpException;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class DigitalTwinConverter {
    public void convertToOldBAMM(@NotNull final DigitalTwin digitalTwin) throws BtpException {
        convert(digitalTwin, true);
    }

    public void convertToNewBAMM(@NotNull final DigitalTwin digitalTwin) throws BtpException {
        convert(digitalTwin, false);
    }

    private void convert(@NotNull final DigitalTwin digitalTwin, @NotNull final boolean toOldBAMM) throws BtpException {
        convertDamages(digitalTwin.getDamages(), toOldBAMM);
        convertRemainingUsefulLifes(digitalTwin.getRemainingUsefulLifes(), toOldBAMM);
        convertAdaptionValues(digitalTwin.getAdaptionValues(), toOldBAMM);
        convertClassifiedLoadSpectra(digitalTwin.getClassifiedLoadSpectra(), toOldBAMM);
    }

    private void convertDamages(@Nullable final List<Damage> damages,
                                @NotNull final boolean toOldBAMM) throws BtpException {
        if (damages == null) {
            return;
        }

        for (final Damage damage : damages) {
            convertDamage(damage, toOldBAMM);
        }
    }

    private void convertRemainingUsefulLifes(@Nullable final List<RemainingUsefulLife> remainingUsefulLifes,
                                             @NotNull final boolean toOldBAMM) throws BtpException {
        if (remainingUsefulLifes == null) {
            return;
        }

        for (final RemainingUsefulLife rul : remainingUsefulLifes) {
            convertRemainingUsefulLife(rul, toOldBAMM);
        }
    }

    private void convertAdaptionValues(@Nullable final List<AdaptionValues> adaptionValues,
                                       @NotNull final boolean toOldBAMM) throws BtpException {
        if (adaptionValues == null) {
            return;
        }

        for (final AdaptionValues adaptionValuesElement : adaptionValues) {
            convertAdaptionValues(adaptionValuesElement, toOldBAMM);
        }
    }

    private void convertClassifiedLoadSpectra(@Nullable final List<ClassifiedLoadSpectrum> classifiedLoadSpectra,
                                              @NotNull final boolean toOldBAMM) throws BtpException {
        if (classifiedLoadSpectra == null) {
            return;
        }

        for (final ClassifiedLoadSpectrum classifiedLoadSpectrum : classifiedLoadSpectra) {
            convertClassifiedLoadSpectrum(classifiedLoadSpectrum, toOldBAMM);
        }
    }

    private void convertDamage(@NotNull final Damage damage, @NotNull final boolean toOldBAMM) throws BtpException {
        convertBammLoaddataSource(damage.getDeterminationLoaddataSource(), toOldBAMM);
        convertBammStatus(damage.getDeterminationStatus(), toOldBAMM);
    }

    private void convertRemainingUsefulLife(@NotNull final RemainingUsefulLife remainingUsefulLife,
                                            @NotNull final boolean toOldBAMM) throws BtpException {
        assertEqualOrNotSet(remainingUsefulLife.getRemainingOperatingTime(),
                            remainingUsefulLife.getRemainingOperatingHours());

        if (toOldBAMM) {
            convertToOldBAMM(remainingUsefulLife);
        } else {
            convertToNewBAMM(remainingUsefulLife);
        }
    }

    private void convertAdaptionValues(@NotNull final AdaptionValues adaptionValues,
                                       @NotNull final boolean toOldBAMM) throws BtpException {
        convertBammStatus(adaptionValues.getStatus(), toOldBAMM);
    }

    private void convertClassifiedLoadSpectrum(@NotNull final ClassifiedLoadSpectrum classifiedLoadSpectrum,
                                               @NotNull final boolean toOldBAMM) throws BtpException {
        if (classifiedLoadSpectrum.getMetadata() == null) {
            return;
        }

        convertBammStatus(classifiedLoadSpectrum.getMetadata().getStatus(), toOldBAMM);
    }

    private void convertBammLoaddataSource(@Nullable final BammLoaddataSource loaddataSource,
                                           @NotNull final boolean toOldBAMM) throws BtpException {
        if (loaddataSource == null) {
            return;
        }

        assertEqualOrNotSet(loaddataSource.getInformationLoadSpectrum(),
                loaddataSource.getInformationOriginLoadSpectrum());

        if (toOldBAMM) {
            convertToOldBAMM(loaddataSource);
        } else {
            convertToNewBAMM(loaddataSource);
        }
    }

    private void convertBammStatus(@Nullable final BammStatus status,
                                   @NotNull final boolean toOldBAMM) throws BtpException {
        if (status == null) {
            return;
        }

        assertEqualOrNotSet(status.getOperatingTime(), status.getOperatingHours());

        if (toOldBAMM) {
            convertToOldBAMM(status);
        } else {
            convertToNewBAMM(status);
        }
    }

    private void convertToOldBAMM(@Nullable final BammLoaddataSource loaddataSource) throws BtpException {
        if(loaddataSource.getInformationLoadSpectrum() != null) {
            if(loaddataSource.getInformationOriginLoadSpectrum() == null) {
                loaddataSource.setInformationOriginLoadSpectrum(loaddataSource.getInformationLoadSpectrum());
            }
            loaddataSource.setInformationLoadSpectrum(null);
        }
    }

    private void convertToNewBAMM(@Nullable final BammLoaddataSource loaddataSource) throws BtpException {
        if(loaddataSource.getInformationOriginLoadSpectrum() != null) {
            if(loaddataSource.getInformationLoadSpectrum() == null) {
                loaddataSource.setInformationLoadSpectrum(loaddataSource.getInformationOriginLoadSpectrum());
            }
            loaddataSource.setInformationOriginLoadSpectrum(null);
        }
    }

    private void convertToOldBAMM(@Nullable final BammStatus status) throws BtpException {
        if(status.getOperatingHours() != null) {
            if(status.getOperatingTime() == null) {
                status.setOperatingTime(status.getOperatingHours().toString());
            }
            status.setOperatingHours(null);
        }
    }

    private void convertToNewBAMM(@Nullable final BammStatus status) throws BtpException {
        if(status.getOperatingTime() != null) {
            if(status.getOperatingHours() == null) {
                try {
                    status.setOperatingHours(Float.parseFloat(status.getOperatingTime()));
                } catch (final Exception exception) {
                    throw new BtpException(exception);
                }
            }
            status.setOperatingTime(null);
        }
    }

    private void convertToOldBAMM(@Nullable final RemainingUsefulLife status) throws BtpException {
        if(status.getRemainingOperatingHours() != null) {
            if(status.getRemainingOperatingTime() == null) {
                status.setRemainingOperatingTime(status.getRemainingOperatingHours().toString());
            }
            status.setRemainingOperatingHours(null);
        }
    }

    private void convertToNewBAMM(@Nullable final RemainingUsefulLife status) throws BtpException {
        if(status.getRemainingOperatingTime() != null) {
            if(status.getRemainingOperatingHours() == null) {
                try {
                    status.setRemainingOperatingHours(Float.parseFloat(status.getRemainingOperatingTime()));
                } catch (final Exception exception) {
                    throw new BtpException(exception);
                }
            }
            status.setRemainingOperatingTime(null);
        }
    }

    private void assertEqualOrNotSet(@Nullable final String value1, @Nullable final String value2) throws BtpException {
        if(value1 == null || value2 == null) {
            return;
        }

        if(!value1.equals(value2)) {
            throw new BtpException("Both values ars set, but different!");
        }
    }

    private void assertEqualOrNotSet(@Nullable final String value1, @Nullable final Float value2) throws BtpException {
        if(value1 == null || value2 == null) {
            return;
        }

        if(!value1.equals(value2.toString())) {
            throw new BtpException("Both values ars set, but different!");
        }
    }
}
