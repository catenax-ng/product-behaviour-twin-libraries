package net.catena_x.btp.sedc.apps.oem.backend.generator;

import net.catena_x.btp.libraries.util.exceptions.BtpException;
import net.catena_x.btp.sedc.model.rawvalues.PeakLoadRawValues;
import net.catena_x.btp.sedc.model.rawvalues.PowerClassesNormalized;

import java.util.Random;

public class TestDataGenerator implements StreamValuesSource<PeakLoadRawValues> {
    private Random randomGenerator = new Random();

    @Override
    public PeakLoadRawValues getNext() throws BtpException {
        final PeakLoadRawValues values = new PeakLoadRawValues();
        values.setPowerClassesNormalized(new PowerClassesNormalized());

        values.setDataCollectionPeriodInMs(10000L);
        values.setAverageEnvironmentTemperatureInC(15.0 + (10.0 * nextRandomNumber()));
        values.setStateOfChargeAtStartNormalized(0.8 * nextRandomNumber());
        values.setStateOfChargeAtEndNormalized(values.getStateOfChargeAtStartNormalized()
                        +  ((1.0 - values.getStateOfChargeAtStartNormalized()) * nextRandomNumber()));

        final PowerClassesNormalized powerClassesNormalized = values.getPowerClassesNormalized();
        powerClassesNormalized.setRecuperation(nextRandomNumber());
        powerClassesNormalized.setLowTorqueLowRevolutions(1E-3 + nextRandomNumber());
        powerClassesNormalized.setLowTorqueHighRevolutions(nextRandomNumber());
        powerClassesNormalized.setHighTorqueLowRevolutions(nextRandomNumber());
        powerClassesNormalized.setHighTorqueHighRevolutions(nextRandomNumber());
        powerClassesNormalized.setPeakLoad(nextRandomNumber());
        values.getPowerClassesNormalized().normalize();

        return values;
    }

    private double nextRandomNumber() {
        return Math.max(0.0, Math.min(Math.abs(randomGenerator.nextGaussian()), 1.0));
    }
}
