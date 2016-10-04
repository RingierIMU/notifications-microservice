package utils;

import com.typesafe.config.ConfigFactory;
import views.html.index;

import java.sql.Timestamp;
import java.util.Date;

public class Util {
    public static boolean webinterfaceEnabled(){
        return (ConfigFactory.load().getBoolean("webinterface.enabled") || ConfigFactory.load().getString("webinterface.enabled").equalsIgnoreCase("true"));
    }

    public static String getCurrentTimestamp() {
        return new Timestamp(new Date().getTime()).toString();
    }
}
