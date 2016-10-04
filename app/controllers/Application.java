package controllers;

import exceptions.FcmMessageNotSentException;
import models.Device;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repositories.FcmMessageRepository;
import utils.Util;
import views.html.index;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Map;

public class Application extends Controller {

    @Inject
    FormFactory formFactory;

    FcmMessageRepository fcmMessageRepository = new FcmMessageRepository();

    public Result index() {
        if (Util.webinterfaceEnabled()) {
            return ok(index.render());
        } else {
            return notFound();
        }
    }

    public Result addDevice() {
        if (!Util.webinterfaceEnabled()) return notFound();

        Form<Device> gcmDeviceForm = formFactory.form(Device.class).bindFromRequest();
        Http.RequestBody body = request().body();
        Map textBody = body.asFormUrlEncoded();
        Device device = (Device) gcmDeviceForm.bind(textBody).get();

        try {
            device.save();
            return redirect(routes.Application.index());
        } catch (PersistenceException persistenceException) {
            return internalServerError("Sorry, this happened while trying to save:\n\n" + persistenceException.getMessage());
        }
    }

    public Result sendMessageToUser() {
        if (!Util.webinterfaceEnabled()) return notFound();

        String user_id, message;

        try {
            DynamicForm requestData = formFactory.form().bindFromRequest();

            user_id = requestData.get("msgUserId");
            message = requestData.get("msgMessage");

            fcmMessageRepository.sendGcmMessageToUser(user_id, message);
            return ok("All messages sent successfully.");
        } catch (FcmMessageNotSentException gmnse) {
            return internalServerError(gmnse.getMessage());
        }
    }

    public Result sendMessageToDevice() {
        if (!Util.webinterfaceEnabled()) return notFound();

        String device_id, to, message;

        try {
            DynamicForm requestData = formFactory.form().bindFromRequest();
            device_id = requestData.get("msgDeviceId");
            message = requestData.get("msgMessage");

            if (device_id != null && !device_id.isEmpty() && (Device.find.byId(Long.parseLong(device_id)) != null)) {
                to = (!(Device.find.byId(Long.parseLong(device_id)).getInstance_id() == null)) ?
                        Device.find.byId(Long.parseLong(device_id)).getInstance_id() : null;
            } else {
                System.err.println(Util.getCurrentTimestamp() + " Device not found: " + device_id);
                return internalServerError("Device not found: " + device_id);
            }

            fcmMessageRepository.sendFcmMessage(to, message);

            return ok("Message sent.");
        } catch (FcmMessageNotSentException gmnse) {
            return internalServerError("Error: " + gmnse.getMessage());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return internalServerError("Error: User input incorrect (Probably text in ID field)...\n" + nfe.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            return internalServerError("Error: \n" + exception.getMessage());
        }

    }

}
