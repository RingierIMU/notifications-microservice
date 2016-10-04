package models;

import com.avaje.ebean.Model;

import java.io.Serializable;
import java.util.List;

/**
 * {
 * "type" : "ALERT_LISTING”
 * "payload": {
 * “ids”: [1,2,3],
 * “entity_id”: 1,
 * “message”: “…”
 * }
 * }
 */
public class FcmMessage extends Model implements Serializable {
    String type;
    Payload payload;

    public FcmMessage() {
    }

    public FcmMessage(String type, List ids, String entity_id, String message) {
        this.type = type;
        this.payload = new Payload(ids, entity_id, message);
    }

    public FcmMessage(String type, Payload payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}