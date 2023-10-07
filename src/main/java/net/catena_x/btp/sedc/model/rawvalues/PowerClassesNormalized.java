package net.catena_x.btp.sedc.model.rawvalues;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PowerClassesNormalized {
    private double recuperation = 0.0;
    private double lowTorqueLowRevolutions = 0.0;
    private double lowTorqueHighRevolutions = 0.0;
    private double highTorqueLowRevolutions = 0.0;
    private double highTorqueHighRevolutions = 0.0;
    private double peakLoad = 0.0;

    public void normalize() {
        final double sum = getSum();
        if(sum > 1E-5) {
            recuperation /= sum;
            lowTorqueLowRevolutions /= sum;
            lowTorqueHighRevolutions /= sum;
            highTorqueLowRevolutions /= sum;
            highTorqueHighRevolutions /= sum;
            peakLoad /= sum;
        }
    }

    public boolean check() {
        Function<Double, Boolean> isAbsoluteOk = x -> x >= 0.0 && x <= 1.0;

        if( !isAbsoluteOk.apply(recuperation)
                || !isAbsoluteOk.apply(lowTorqueLowRevolutions)
                || !isAbsoluteOk.apply(lowTorqueHighRevolutions)
                || !isAbsoluteOk.apply(highTorqueLowRevolutions)
                || !isAbsoluteOk.apply(highTorqueHighRevolutions)
                || !isAbsoluteOk.apply(peakLoad) ) {
            return false;
        }

        return Math.abs(getSum()) < 0.01;
    }

    private double getSum() {
        return recuperation + lowTorqueLowRevolutions + lowTorqueHighRevolutions
                + highTorqueLowRevolutions + highTorqueHighRevolutions + peakLoad;
    }
}