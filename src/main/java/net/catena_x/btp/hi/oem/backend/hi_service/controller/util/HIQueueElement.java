package net.catena_x.btp.hi.oem.backend.hi_service.controller.util;

import net.catena_x.btp.hi.oem.backend.hi_service.collector.util.HIUpdateOptions;

public record HIQueueElement(
        HIQueueState queueState,
        HIUpdateOptions options
) {}