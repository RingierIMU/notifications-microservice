package models;

import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class HttpAuthUser implements Serializable {
    String username;
    String password;

    public HttpAuthUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static HttpAuthUser findByAuthToken(String authTokenHeaderValue) {
        String auth = authTokenHeaderValue.substring(6); // skip over String "Basic "
        byte[] decodedAuth = Base64.decodeBase64(auth);
        String[] credentials;
        try {
            credentials = new String(decodedAuth, "UTF-8").split(":");
            if (credentials.length == 2) {
                return new HttpAuthUser(credentials[0], credentials[1]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
