package net.catena_x.btp.hi.oem.frontend.model.dto.statistics;

public record HIHistogram(
        int countGreen, int countYellow, int countRed, int countUnknown
) {}
