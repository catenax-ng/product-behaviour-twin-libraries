package net.catena_x.btp.hi.oem.backend.hi_service.receiver;

import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.HINotificationFromSupplierContent;
import net.catena_x.btp.hi.oem.backend.hi_service.notifications.dto.supplierhiservice.items.HealthIndicatorOutput;
import net.catena_x.btp.hi.oem.common.model.dto.calculation.HICalculation;
import net.catena_x.btp.hi.oem.common.model.dto.calculation.HICalculationTable;
import net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent.HIKAInputs;
import net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent.HIKAOutput;
import net.catena_x.btp.hi.oem.common.model.dto.knowledgeagent.HIKAOutputs;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicle;
import net.catena_x.btp.hi.oem.common.model.dto.vehicle.HIVehicleTable;
import net.catena_x.btp.hi.oem.common.model.enums.HICalculationStatus;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.notification.dto.Notification;
import net.catena_x.btp.libraries.util.datahelper.DataHelper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class HIResultProcessor {
    @Autowired private HICalculationTable hiCalculationTable;
    @Autowired private HIVehicleTable hiVehicleTable;

    private final Logger logger = LoggerFactory.getLogger(HIResultProcessor.class);

    public void process(@NotNull final Notification<HINotificationFromSupplierContent> result,
                        @NotNull final Runnable notifyProcessed) {
        try {
            new Thread(() -> processingThread(result, notifyProcessed)).start();
        } catch (final Exception exception) {
            logError(exception.getMessage());
            notifyProcessed.run();
        }
    }

    public void process(@NotNull final HIKAOutputs results, @NotNull final HIKAInputs inputs) {
        try {
            new Thread(() -> processingThread(results, inputs)).start();
        } catch (final Exception exception) {
            logError(exception.getMessage());
        }
    }

    private void processingThread(@NotNull final Notification<HINotificationFromSupplierContent> result,
                                  @NotNull final Runnable notifyProcessed) {
        try {
            assertHeaderAndBody(result);
            this.processInternal(result.getContent(), getReferenceId(result));
        } catch (final OemHIException exception) {
            logError(exception.getMessage());
        }

        notifyProcessed.run();
    }

    private void processingThread(@NotNull final HIKAOutputs results, @NotNull final HIKAInputs inputs) {
        try {
            this.processInternal(results, inputs);
        } catch (final OemHIException exception) {
            logError(exception.getMessage());
        }
    }

    private void processInternal(@NotNull final HINotificationFromSupplierContent result,
                                 @NotNull final String referenceId)
            throws OemHIException {

        final HICalculation calculation = getCalculationFromId(referenceId);

        checkOutputs(result.getHealthIndicatorOutputs(), referenceId);

        hiCalculationTable.updateStatusNewTransaction(referenceId, HICalculationStatus.CALCULATED);
        processAllOutputs(result.getHealthIndicatorOutputs(), calculation);
        hiCalculationTable.updateStatusNewTransaction(referenceId, HICalculationStatus.READY);
    }

    private void processInternal(@NotNull final HIKAOutputs results, @NotNull final HIKAInputs inputs)
            throws OemHIException {
        checkOutputs(results, inputs.getRequestId());

        hiCalculationTable.updateStatusNewTransaction(inputs.getRequestId(), HICalculationStatus.CALCULATED);
        processAllOutputs(results, inputs);
        hiCalculationTable.updateStatusNewTransaction(inputs.getRequestId(), HICalculationStatus.READY);
    }

    private HICalculation getCalculationFromId(@NotNull final String referenceId) throws OemHIException {
        final HICalculation calculation = hiCalculationTable.getByIdNewTransaction(referenceId);
        if(calculation == null) {
            throw new OemHIException("No pending calculation found for " + referenceId + "!");
        }
        return calculation;
    }

    private void checkOutputs(@Nullable final List<HealthIndicatorOutput> outputs,
                              @NotNull final String referenceId) throws OemHIException {
        if(DataHelper.isNullOrEmpty(outputs)) {
            hiCalculationTable.updateStatusNewTransaction(referenceId, HICalculationStatus.FAILED_EXTERNAL);
            throw new OemHIException("HI result for " + referenceId
                    + " is empty or not present (maybe a format or calculation error occurred)!");
        }
    }

    private void checkOutputs(@Nullable final HIKAOutputs outputs,
                              @NotNull final String referenceId) throws OemHIException {
        if(DataHelper.isNullOrEmpty(outputs.getResults())) {
            hiCalculationTable.updateStatusNewTransaction(referenceId, HICalculationStatus.FAILED_EXTERNAL);
            throw new OemHIException("HI result for " + referenceId
                    + " is empty or not present (maybe a format or calculation error occurred)!");
        }
    }

    private void processAllOutputs(@Nullable final List<HealthIndicatorOutput> outputs,
                                   @NotNull final HICalculation calculation) throws OemHIException {
        for (final HealthIndicatorOutput output: outputs) {
            processSingleOutput(output, calculation);
        }
    }

    private void processAllOutputs(@NotNull final HIKAOutputs results,
                                   @NotNull final HIKAInputs inputs) throws OemHIException {
        for (final HIKAOutput output : results.getResults()) {
            processSingleOutput(output, inputs);
        }
    }

    private void assertHeaderAndBody(@Nullable final Notification<HINotificationFromSupplierContent> result)
        throws OemHIException {

        if (result == null) {
            throw new OemHIException("Notification is null!");
        }

        if (result.getHeader() == null) {
            throw new OemHIException("Notification header is not present!");
        }

        if (result.getContent() == null) {
            throw new OemHIException("Notification content is not present!");
        }
    }

    private void assertContentReferenceId(@Nullable final Notification<HINotificationFromSupplierContent> result)
            throws OemHIException {
        if (result.getContent().getRequestRefId() == null) {
            throw new OemHIException("No reference id in Notification content present!");
        }
    }

    private String getReferenceId(@NotNull final Notification<HINotificationFromSupplierContent> result)
            throws OemHIException {

        if(result.getHeader().getReferencedNotificationID() == null) {
            assertContentReferenceId(result);
            return result.getHeader().getReferencedNotificationID();
        }

        return result.getHeader().getReferencedNotificationID();
    }

    private void processSingleOutput(@NotNull final HealthIndicatorOutput output,
                                     @NotNull final HICalculation calculation) throws OemHIException {

        final String vehicleId = hiVehicleTable.getByGearboxIdNewTransaction(output.getComponentId()).getVehicleId();
        hiVehicleTable.appendHealthindicatorsNewTransaction(vehicleId, output, calculation.getCalculationTimestamp(),
                calculation.getCalculationSyncCounterMax());

        //TODO: Test above implementation for writing to hi database (and remove logging).
        final StringBuilder hiValues = new StringBuilder();

        if( output.getHealthIndicatorValues().length > 0) {
            hiValues.append(output.getHealthIndicatorValues()[0]);
            for (int i = 1; i < output.getHealthIndicatorValues().length; i++) {
                hiValues.append(", ");
                hiValues.append(output.getHealthIndicatorValues()[i]);
            }
        }

        logger.info("  > " + calculation.getId() + ": gearbox[" + output.getComponentId()
                + "]: HI={" + hiValues.toString() + "}" );
    }

    private void processSingleOutput(@NotNull final HIKAOutput output, @NotNull final HIKAInputs inputs)
            throws OemHIException {

        final HIVehicle vehicle = hiVehicleTable.getByVanNewTransaction(output.getVan());
        if(vehicle == null) {
            throw new OemHIException("Vehicle with VAN " + output.getVan() + "was not registered!");
        }

        final String vehicleId = vehicle.getVehicleId();
        hiVehicleTable.appendHealthindicatorsNewTransaction(
                vehicleId,
                new HealthIndicatorOutput("KNOWLEDGEAGENT_0", vehicle.getGearboxId(), output.getHealthIndicatorValues()),
                inputs.getCalculationTimestamp(),
                inputs.getMaxSyncCounter());

        //TODO: Test above implementation for writing to hi database (and remove logging).
        final StringBuilder hiValues = new StringBuilder();

        if( output.getHealthIndicatorValues().length > 0) {
            hiValues.append(output.getHealthIndicatorValues()[0]);
            for (int i = 1; i < output.getHealthIndicatorValues().length; i++) {
                hiValues.append(", ");
                hiValues.append(output.getHealthIndicatorValues()[i]);
            }
        }

        logger.info("  > gearbox[" + vehicle.getGearboxId() + "]: HI={" + hiValues.toString() + "}" );
    }

    private synchronized void logError(@NotNull final String message) {
        logger.error(message);
    }
}
