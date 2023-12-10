package rawconsole;

import java.io.IOException;

public class RawConsole {

    static {
        System.loadLibrary("rawconsole");
    }

    public static native int getRawKeyId();

    public static RawKeys getRawKey() {
        return RawKeys.values()[getRawKeyId()];
    }

    public static void clearConsole() {
        final String os = System.getProperty("os.name");

        try {
            if (os.contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}