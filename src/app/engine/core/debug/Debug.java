package app.engine.core.debug;

import app.assets.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Debug {

    private static String getTimeStamp() {
        return new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(Calendar.getInstance().getTime());
    }

    private static void updateDebugText(String msg, int level) {
        if (GameSettings.DEBUG_MODE) {
            Text text = new Text(msg + "\n");

            Color color;
            switch (level) {
                case 0:
                    color = Color.BLACK;
                    break;
                case 1:
                    color = Color.ORANGE;
                    break;
                case 2:
                    color = Color.RED;
                    break;
                default:
                    color = Color.BLACK;
                    break;
            }

            text.setFill(color);
            DebugPane.debugTextArea.getChildren().add(text);
        }
    }

    public static void log (Object ...args) {

        StringBuilder s = new StringBuilder(getTimeStamp() + " [LOG] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.out.println(s);
        updateDebugText(s.toString(), 0);
    }

    public static void warn (Object ...args) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        StringBuilder s = new StringBuilder(getTimeStamp()  + " [WARN] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.err.println(s);
        updateDebugText(s.toString(), 1);
    }

    public static void error (Object ...args) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        StringBuilder s = new StringBuilder(getTimeStamp()  + " [ERROR] ");
        for (Object o : args) {
            s.append(o.toString()).append(" ");
        }
        System.err.println(s);
        updateDebugText(s.toString(), 2);
    }

}
