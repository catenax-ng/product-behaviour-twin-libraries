package net.catena_x.btp.hi.oem.frontend.model.dao.vehicle.statistics;

public record HIFHistogram(
        int countGreen, int countYellow, int countRed, int countUnknown
) {}
