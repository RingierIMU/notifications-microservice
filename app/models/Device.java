package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Device extends Model implements Serializable {
    public static Finder<Long, Device> find = new Finder<Long, Device>(Device.class);
    @Id
    private Long id;
    private Long user_id;
    private String operating_system;
    private String operating_system_version;
    private String manufacturer;
    private String model;
    private String instance_id;

    public Device(Long id, Long user_id, String operating_system, String operating_system_version, String manufacturer, String model, String instance_id) {
        this.id = id;
        this.user_id = user_id;
        this.operating_system = operating_system;
        this.operating_system_version = operating_system_version;
        this.manufacturer = manufacturer;
        this.model = model;
        this.instance_id = instance_id;
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

    public String getOperating_system() {
        return operating_system;
    }

    public void setOperating_system(String operating_system) {
        this.operating_system = operating_system;
    }

    public String getOperating_system_version() {
        return operating_system_version;
    }

    public void setOperating_system_version(String operating_system_version) {
        this.operating_system_version = operating_system_version;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }
}
