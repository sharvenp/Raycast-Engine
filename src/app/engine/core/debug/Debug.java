package app.engine.core.debug;

import app.assets.GameSettings;

public class Debug {

    public static void log (Object ...args) {

        if (!GameSettings.DEBUG_LOG) { return; }

        StringBuilder s = new StringBuilder("[LOG] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.out.println(s);
    }

    public static void warn (Object ...args) {

        if (!GameSettings.DEBUG_WARN) { return; }

        StringBuilder s = new StringBuilder("[WARN] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.err.println(s);
    }

    public static void error (Object ...args) {

        if (!GameSettings.DEBUG_ERROR) { return; }

        StringBuilder s = new StringBuilder("[ERROR] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.err.println(s);
    }

}
