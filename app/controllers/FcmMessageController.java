package controllers;

import exceptions.FcmMessageNotSentException;
import models.Device;
import play.mvc.*;
import repositories.FcmMessageRepository;

@Security.Authenticated(Secured.class)
public class FcmMessageController extends Controller {

    FcmMessageRepository fcmMessageRepository = new FcmMessageRepository();

    @BodyParser.Of(BodyParser.Json.class)
    public Result sendMessage() {
        String to, message;

        try {
            Http.RequestBody body = request().body();
            message = body.asJson().get("message").asText();
            to = body.asJson().get("to").asText();

            fcmMessageRepository.sendFcmMessage(to, message);
            return ok("All messages sent successfully.");
        } catch (FcmMessageNotSentException gmnse) {
            return internalServerError(gmnse.getMessage());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result sendMessageToUser(Long user_id) {
        String message;

        try {
            Http.RequestBody body = request().body();
            message = body.asJson().toString();

            fcmMessageRepository.sendFcmMessageToUser(String.valueOf(user_id), message);
            return ok("All messages sent successfully.");
        } catch (FcmMessageNotSentException gmnse) {
            return internalServerError(gmnse.getMessage());
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result sendMessageToDevice(Long device_id) {
        String to, message;

        try {
            Http.RequestBody body = request().body();
            message = body.asJson().toString();

            to = (!(Device.find.byId(device_id).getInstance_id() == null)) ?
                    Device.find.byId(device_id).getInstance_id() : null;

            fcmMessageRepository.sendFcmMessage(to, message);

            return ok("Message sent.");
        } catch (FcmMessageNotSentException gmnse) {
            return internalServerError("Error: Could net send message to device " + device_id +
                    ", either message or to field was empty.");
        } catch (Exception exception) {
            exception.printStackTrace();
            return internalServerError("Error: " + exception.getMessage());
        }
    }
}
