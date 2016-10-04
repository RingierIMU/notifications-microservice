package repositories;

import models.Device;

import javax.persistence.PersistenceException;

public class DeviceRepository {
    public void removeDevice(Long device_id) {
        try {
            Device.find.ref(device_id).delete();
        } catch (PersistenceException persistenceException) {
            // TODO handle this...
        }
    }

    public void removeDevice(String instance_id) {
        try {
            Device device = Device.find.where().eq("instance_id", instance_id).findUnique();
            if (device != null)
                removeDevice(device.getId());
        } catch (PersistenceException persistenceException) {
            // TODO handle this...
        }
    }

}
