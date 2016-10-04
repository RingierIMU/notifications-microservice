package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.ByteStreams;
import com.typesafe.config.ConfigFactory;
import exceptions.DeviceNotRegisteredException;
import models.Device;
import models.FcmMessage;
import play.libs.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;

public class FcmSender {

    public static final String API_KEY = ConfigFactory.load().getString("fcm.apikey");

    public void sendMessage(String message, String to) throws DeviceNotRegisteredException {
        FcmMessage fcmMessage;
        Device device = new Device(null, null, null, null, null, null, to);

        try {
            fcmMessage = Json.fromJson(Json.parse(message), FcmMessage.class);
        } catch (RuntimeException re) {
            System.err.println(Util.getCurrentTimestamp() + " Error parsing Json from user: " + message);
            return;
        }

        sendMessage(device, fcmMessage);
    }

    public void sendMessage(Device device, FcmMessage fcmMessage) throws DeviceNotRegisteredException {
        try {
            ObjectNode jGcmData = Json.newObject(), jData = Json.newObject(), jPayload = Json.newObject();
            JsonNode jResponse;

            if (device != null && device.getInstance_id() != null && !device.getInstance_id().isEmpty()) {
                jGcmData.put("to", device.getInstance_id());
            } else {
                System.err.println(Util.getCurrentTimestamp() + " Device does not have a valid instance ID.");
                return;
            }

            if (fcmMessage.getPayload() != null) {
                jData.put("message", fcmMessage.getPayload().getMessage());
                jData.put("type", fcmMessage.getType());
                jPayload.put("entity_id", fcmMessage.getPayload().getEntity_id());
                jData.put("payload", jPayload);
                jGcmData.put("data", jData);
            } else {
                System.err.println(Util.getCurrentTimestamp() + " FcmMessage does not have payload.");
                return;
            }

            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            System.out.println(Util.getCurrentTimestamp() + " Sent this to Google: " + jGcmData.toString());

            InputStream inputStream = conn.getInputStream();
            String response = new String(ByteStreams.toByteArray(inputStream));
            int responseCode = conn.getResponseCode();
            // Todo Handle the response code
            conn.disconnect();

            jResponse = Json.parse(response);

            if (jResponse.get("success").toString().equalsIgnoreCase("0")) {
                if (jResponse.get("results").get(0).get("error").textValue().contains("NotRegistered")) {
                    throw new DeviceNotRegisteredException("Device " + device.getInstance_id() +
                            " with instance ID " + device.getInstance_id() + " is not registered with GCM. User possibly uninstalled the app. Device has been removed.");
                }

                if (jResponse.get("results").get(0).get("error").textValue().contains("InvalidRegistration")) {
                    throw new DeviceNotRegisteredException("Device " + device.getInstance_id() +
                            " with instance ID " + device.getInstance_id() + " is invalidly registered with GCM. Probably a problem while registering with GCM. Device has been removed.");
                }
            }

            System.out.println(Util.getCurrentTimestamp() + " Got this response from Google: " + response);
        } catch (UnknownServiceException use) {
            System.err.println(Util.getCurrentTimestamp() + " Unknown service.");
            use.printStackTrace();
        } catch (IOException ioException) {
            System.err.println(Util.getCurrentTimestamp() + " Unable to send GCM message. Please ensure that API_KEY " +
                    "has been replaced by the server  API key, and that the device's registration token is correct (if specified).");
            ioException.printStackTrace();
        }
    }

}
