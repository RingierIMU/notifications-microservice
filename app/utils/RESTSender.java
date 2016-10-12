package utils;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;

public class RESTSender {

    public void sendMessage(String endpoint, String credentials, String payload) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Basic " + credentials);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(payload.getBytes());

            System.out.println(Util.getCurrentTimestamp() + " Sent this: " + payload + " to: " + endpoint);

            InputStream inputStream = conn.getInputStream();
            String response = new String(ByteStreams.toByteArray(inputStream));
            int responseCode = conn.getResponseCode();
            conn.disconnect();

            System.out.println(Util.getCurrentTimestamp() + " Got this response: " + response
                    + " from: " + endpoint
                    + " with response code: " + responseCode);
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
