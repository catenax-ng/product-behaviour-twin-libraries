package net.catena_x.btp.rul.oem.backend.rul_service.notifications.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum NotificationClassification {
    @JsonProperty("EndurancePredictor")
    RULSERVICETOSUPPLIER,

    @JsonProperty("EndurancePredictorResult")
    RULRESULTFROMSUPPLIER,

    @JsonProperty("RemainingUsefulLifeResult")
    RULRESULTTOREQUESTER
}