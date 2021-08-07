package app.engine.core.debug;

import app.assets.GameSettings;

public class Debug {

    public static void log (Object ...args) {

        StringBuilder s = new StringBuilder("[LOG] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.out.println(s);
    }

    public static void warn (Object ...args) {

        StringBuilder s = new StringBuilder("[WARN] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.err.println(s);
    }

    public static void error (Object ...args) {

        StringBuilder s = new StringBuilder("[ERROR] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.err.println(s);
    }

}
