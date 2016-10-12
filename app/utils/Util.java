package utils;

import com.typesafe.config.ConfigFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Util {
    public static final String FCM_SERVER = "fcm-xmpp.googleapis.com";
    public static final int FCM_PORT = 5235;
    public static final String FCM_ELEMENT_NAME = "gcm";
    public static final String FCM_NAMESPACE = "google:mobile:data";
    public static final String FCM_SERVER_CONNECTION = "gcm.googleapis.com";
    public static final String FCM_SERVER_KEY = ConfigFactory.load().getString("fcm.serverkey");
    public static final String FCM_SENDERID = ConfigFactory.load().getString("fcm.senderid");

    public static boolean webinterfaceEnabled() {
        return (ConfigFactory.load().getBoolean("webinterface.enabled") ||
                ConfigFactory.load().getString("webinterface.enabled").equalsIgnoreCase("true"));
    }

    public static String getCurrentTimestamp() {
        return new Timestamp(new Date().getTime()).toString();
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}
