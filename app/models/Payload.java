package models;

import com.avaje.ebean.Model;

import java.io.Serializable;
import java.util.List;

public class Payload extends Model implements Serializable {
    List ids;
    String entity_id;
    String message;

    public Payload() {
    }

    public Payload(List ids, String entity_id, String message) {
        this.ids = ids;
        this.entity_id = entity_id;
        this.message = message;
    }

    public List getIds() {
        return ids;
    }

    public void setIds(List ids) {
        this.ids = ids;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
