package net.catena_x.btp.hi.oem.backend.hi_service.controller.util;

import net.catena_x.btp.hi.oem.backend.hi_service.collector.HIDataCollector;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.enums.UpdateInfo;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.util.HICollectorOptionReader;
import net.catena_x.btp.hi.oem.backend.hi_service.collector.util.HIUpdateOptions;
import net.catena_x.btp.hi.oem.util.exceptions.OemHIException;
import net.catena_x.btp.libraries.util.apihelper.ApiHelper;
import net.catena_x.btp.libraries.util.apihelper.model.DefaultApiResult;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class HIJobRunner {
    @Autowired private HIDataCollector dataCollector;
    @Autowired private HIQueue queue;
    @Autowired private ApiHelper apiHelper;
    @Autowired private HICollectorOptionReader hiCollectorOptionReader;

    public ResponseEntity<DefaultApiResult> startJob() {
        try {
            return startJobInternal(queue.addNewJobToQueue(null));
        } catch (final OemHIException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    public ResponseEntity<DefaultApiResult> startJob(@Nullable final HIUpdateOptions options) {
        if(options == null) {
            return startJob();
        }

        try {
            return startJobInternal(queue.addNewJobToQueue(options));
        } catch (final OemHIException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    public ResponseEntity<DefaultApiResult> setJobFinishedAndStartQueued() {
        try {
            return startJobInternal(queue.removeJobFromQueueReturnNextQueuedElement());
        } catch (final OemHIException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    public ResponseEntity<DefaultApiResult> resetQueue() {
        try {
            //Removing queued and running element (if existing).
            queue.removeJobFromQueueReturnNextQueuedElement();
            queue.removeJobFromQueueReturnNextQueuedElement();
            return apiHelper.ok("Queue is reset.");
        } catch (final OemHIException exception) {
            return apiHelper.failed(exception.getMessage());
        }
    }

    private ResponseEntity<DefaultApiResult> startJobInternal(@NotNull final HIQueueElement currentQueueElement)
            throws OemHIException {

        switch(currentQueueElement.queueState()) {
            case NOT_RUNNING: {
                //No action needed.
                return apiHelper.ok("Ok.");
            }

            case WAITING: {
                //No action needed.
                return apiHelper.ok("External hi calculation is queued and " +
                        "will be started after current running job.");
            }

            case RUNNING: {
                switch (startJobInternal(currentQueueElement.options())) {
                    case UPDATE_STARTED: {
                        if(currentQueueElement.options().isUseKnowledgeAgent()) {
                            setJobFinishedAndStartQueued();
                        }

                        return apiHelper.ok("Started external hi calculation.");
                    }

                    case NOTHING_TO_UPDATE: {
                        setJobFinishedAndStartQueued(); //TODO: Process result, if new job is started.
                        return apiHelper.ok("No updated vehicles found. Nothing to calculate.");
                    }

                    default: {
                        throw new OemHIException("Unknown update state!");
                    }
                }
            }

            default: {
                throw unknownError();
            }
        }
    }

    private UpdateInfo startJobInternal(@Nullable final HIUpdateOptions options) throws OemHIException {
        try {
            return dataCollector.doUpdate(options);
        } catch (final Exception exception) {
            try {
                setJobFinishedAndStartQueued();
            } catch (final Exception nestedException) {
                throw new OemHIException("Start of job and queued job failed: "
                        + exception.getMessage() + nestedException.getMessage());
            }

            throw new OemHIException(exception);
        }
    }

    private OemHIException unknownError() {
        return new OemHIException("External hi calculation cannot be started. Unknown error!");
    }
}
