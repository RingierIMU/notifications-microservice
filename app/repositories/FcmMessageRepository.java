package repositories;

import exceptions.DeviceNotRegisteredException;
import exceptions.FcmMessageNotSentException;
import models.Device;
import utils.FcmSender;
import utils.Util;

import java.util.List;

public class FcmMessageRepository {

    private FcmSender fcmSender = new FcmSender();
    private DeviceRepository deviceRepository = new DeviceRepository();

    public void sendFcmMessageToUser(String user_id, String message) throws FcmMessageNotSentException {
        Integer successfulMessagesSent = 0;

        List<Device> deviceList = Device.find.where().eq("user_id", user_id).findList();

        if (!deviceList.isEmpty()) {
            String device_id;
            for (Device device : deviceList) {
                device_id = device.getInstance_id();

                try {
                    sendFcmMessage(device_id, message);
                    successfulMessagesSent++;
                } catch (FcmMessageNotSentException gmnse) {
                    System.err.println(Util.getCurrentTimestamp() +
                            " Either message or to field was empty." +
                            " Message: " + message.toString() +
                            " Device_ID:  " + device_id);
                    throw gmnse;
                }
            }
        }

        if (successfulMessagesSent == deviceList.size()) {
            return;
        } else if (successfulMessagesSent < deviceList.size()) {
            System.out.println(Util.getCurrentTimestamp() + " Sent " + successfulMessagesSent +
                    " message successfully to user_id: " + user_id);
            return;
        } else {
            throw new FcmMessageNotSentException("Sending messages failed to all devices that belong to user_id " + user_id);
        }

    }

    public void sendFcmMessage(String to, String message) throws FcmMessageNotSentException {
        try {
            if (message != null && to != null && !message.isEmpty() && !to.isEmpty()) {
                fcmSender.sendMessage(message, to);
            } else {
                String errorMessage = Util.getCurrentTimestamp() +
                        " Either message or to field was empty." +
                        " Message: " + message.toString() +
                        " Device_ID: " + to;
                System.err.println(errorMessage);
                throw new FcmMessageNotSentException(errorMessage);
            }
        } catch (DeviceNotRegisteredException gdnre) {
            deviceRepository.removeDevice(to);
            throw new FcmMessageNotSentException("Device " + to + " not registered with GCM: " + gdnre.getMessage());
        }
    }
}
