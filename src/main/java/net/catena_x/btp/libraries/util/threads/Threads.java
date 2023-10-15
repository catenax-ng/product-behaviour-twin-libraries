package net.catena_x.btp.libraries.util.threads;

public final class Threads {
    public static boolean sleepWithoutExceptions(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (final InterruptedException exception) {
            return false;
        }
    }
}