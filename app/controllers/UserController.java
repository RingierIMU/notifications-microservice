package controllers;

import models.User;
import play.libs.Json;
import play.mvc.*;
import repositories.UserRepository;
import utils.Util;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class UserController extends Controller {

    UserRepository userRepository = new UserRepository();
    List<User> deviceList;

    public Result users() {
        try {
            deviceList = new ArrayList<>();
            deviceList = User.find.all();
            return ok(Json.toJson(deviceList));
        } catch (PersistenceException persistenceException) {
            return internalServerError("Could not load users from database: " + persistenceException.getMessage());
        }
    }

    public Result user(Long id) {
        try {
            User user = User.find.byId(id);
            if (user != null) {
                return ok(Json.toJson(user));
            } else {
                return notFound("Could not find this user.");
            }
        } catch (PersistenceException persistenceException) {
            return internalServerError("Could not find this user: " + persistenceException.getMessage());
        }
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result createUser() {
        User user;
        Http.RequestBody body = request().body();
        user = Json.fromJson(body.asJson(), User.class);
        try {
            user.save();
            return ok(Json.toJson(user));
        } catch (PersistenceException persistenceException) {
            return internalServerError(Json.toJson(user));
        }
    }

    @Security.Authenticated(Secured.class)
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateUser(Long id) {
        User userFromRequest, userFromDb;
        Http.RequestBody body = request().body();

        if (body != null && body.asJson() != null) {
            userFromRequest = Json.fromJson(body.asJson(), User.class);
            userFromDb = User.find.where().eq("instance_id", userFromRequest.getId()).findUnique();

            if (userFromDb != null) {
                try {
                    userFromRequest.setId(userFromDb.getId());
                    userFromRequest.update();
                    return ok(Json.toJson(userFromDb));
                } catch (PersistenceException persistenceException) {
                    return internalServerError(Json.toJson(userFromDb));
                }
            } else {
                try {
                    userFromRequest.save();
                    return ok(Json.toJson(userFromRequest));
                } catch (PersistenceException persistenceException) {
                    return internalServerError(Json.toJson(userFromRequest));
                }

            }
        } else {
            return internalServerError("Something went wrong.");
        }
    }

    public Result deleteUser(Long id) {
        try {
            User user = User.find.byId(id);
            userRepository.deleteUser(id);
            return ok(Json.toJson(user));
        } catch (PersistenceException persistenceException) {
            return internalServerError("Error deleting device: " + persistenceException.getMessage());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result togglePuNo() {

        Long user_id = null;
        boolean puno_flag = false;
        User user = null;

        try {
            Http.RequestBody body = request().body();
            if (body.asJson().has("user_id"))
                user_id = body.asJson().get("user_id").asLong();
            if (body.asJson().has("receive_push_notifications"))
                puno_flag = body.asJson().get("receive_push_notifications").asBoolean();

            if (user_id != null) user = User.find.byId(user_id);

            if (user != null) {
                userRepository.togglePuNo(user, puno_flag);
                return ok(Json.toJson(user));
            } else {
                return ok(Json.toJson(new User(Util.getUniqueId(), false)));
            }
        } catch (PersistenceException persistenceException) {
            return internalServerError("Error deleting device: " + persistenceException.getMessage());
        }
    }
}
