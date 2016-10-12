package controllers;

import models.Device;
import models.User;
import play.libs.Json;
import play.mvc.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class DeviceController extends Controller {

    List<Device> deviceList;

    public Result devices() {
        try {
            deviceList = new ArrayList<>();
            deviceList = Device.find.all();
            return ok(Json.toJson(deviceList));
        } catch (PersistenceException persistenceException) {
            return internalServerError("Could not load devices from database: " + persistenceException.getMessage());
        }
    }

    public Result device(Long id) {
        try {
            Device device = Device.find.byId(id);
            if (device != null) {
                return ok(Json.toJson(device));
            } else {
                return notFound("Could not find this device.");
            }
        } catch (PersistenceException persistenceException) {
            return internalServerError("Could not find this device: " + persistenceException.getMessage());
        }
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createDevice() {
        Device device;
        Http.RequestBody body = request().body();
        device = Json.fromJson(body.asJson(), Device.class);
        try {
            if (device.getUser_id() == 0 || device.getUser_id() == null) {
                User user = new User(0l, true);
                user.save();
                user.refresh();
                device.setUser_id(user.getId());
            }
            device.save();
            return ok(Json.toJson(device));
        } catch (PersistenceException persistenceException) {
            return internalServerError(Json.toJson(device));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateDevice(Long id) {
        // TODO This needs fixing, it does not take the ID provided by the URL into account
        Device device;
        Http.RequestBody body = request().body();

        if (body != null && body.asJson() != null) {
            device = Json.fromJson(body.asJson(), Device.class);

            try {
                device.update();
                return ok(Json.toJson(device));
            } catch (PersistenceException persistenceException) {
                return internalServerError("Error updating the device: " + persistenceException.getMessage());
            }
        } else {
            return internalServerError("Something went wrong.");
        }
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateMyDevice() {
        Device deviceFromRequest, deviceFromDb;
        Http.RequestBody body = request().body();

        if (body != null && body.asJson() != null) {
            deviceFromRequest = Json.fromJson(body.asJson(), Device.class);
            deviceFromDb = Device.find.where().eq("instance_id", deviceFromRequest.getInstance_id()).findUnique();

            if (deviceFromDb != null) {
                try {
                    deviceFromRequest.setId(deviceFromDb.getId());
                    deviceFromRequest.update();
                    return ok(Json.toJson(deviceFromDb));
                } catch (PersistenceException persistenceException) {
                    return internalServerError(Json.toJson(deviceFromDb));
                }
            } else {
                try {
                    if (deviceFromRequest.getUser_id() == 0 || deviceFromRequest.getUser_id() == null) {
                        User user = new User(0l, true);
                        user.save();
                        user.refresh();
                        deviceFromRequest.setUser_id(user.getId());
                    }
                    deviceFromRequest.save();
                    return ok(Json.toJson(deviceFromRequest));
                } catch (PersistenceException persistenceException) {
                    return internalServerError(Json.toJson(deviceFromRequest));
                }

            }
        } else {
            return internalServerError("Something went wrong.");
        }
    }

    public Result deleteDevice(Long id) {
        try {
            Device device = Device.find.byId(id);
            Device.find.ref(id).delete();
            return ok(Json.toJson(device));
        } catch (PersistenceException persistenceException) {
            return internalServerError("Error deleting device: " + persistenceException.getMessage());
        }
    }

    public Result devicesOfUser(Long user_id) {
        try {
            List<Device> deviceList = Device.find.where().eq("user_id", user_id).findList();

            if (deviceList != null) {
                return ok(Json.toJson(deviceList));
            } else {
                return notFound("Could not find devices for user with id: " + user_id);
            }

        } catch (PersistenceException persistenceException) {
            return internalServerError("Error fetching devices for user: " + persistenceException.getMessage());
        }

    }

}
