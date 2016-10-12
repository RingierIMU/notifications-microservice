package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Blob extends Model implements Serializable {

    public static Finder<Long, Blob> find = new Finder<Long, Blob>(Blob.class);

    @Id
    private Long id;
    private Long user_id;
    private String data;

    public Blob(Long id, Long user_id, String data) {
        this.id = id;
        this.user_id = user_id;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
