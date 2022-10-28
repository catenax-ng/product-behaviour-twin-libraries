package net.catena_x.btp.libraries.oem.backend.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SQLFormatter {
    // TODO make generic method (not that easy because String::valueOf has different overloads)
    public static String doubleArrayToSQLArrayStr(double[] array) {
        return "ARRAY [" +
                Arrays.stream(array).mapToObj(String::valueOf).collect(Collectors.joining(",")) +
                "]";
    }
}
