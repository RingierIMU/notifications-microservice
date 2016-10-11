package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class User extends Model implements Serializable {
    public static Finder<Long, User> find = new Finder<Long, User>(User.class);

    @Id
    private String id;
    private boolean receive_push_notifications;

    public User(String id, boolean receive_push_notifications) {
        this.id = id;
        this.receive_push_notifications = receive_push_notifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReceive_push_notifications() {
        return receive_push_notifications;
    }

    public void setReceive_push_notifications(boolean receive_push_notifications) {
        this.receive_push_notifications = receive_push_notifications;
    }
}
