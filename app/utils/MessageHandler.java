package utils;

import models.Blob;
import models.UpstreamMessage;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import repositories.BlobRepository;
import xmpp.GcmPacketExtension;
import xmpp.MessageHelper;

import java.util.Map;

public class MessageHandler {

    private XMPPTCPConnection mConnection;
    private BlobRepository mBlobRepository = new BlobRepository();

    public MessageHandler(XMPPTCPConnection xmpptcpConnection) {
        this.mConnection = xmpptcpConnection;
    }

    public void handleUpstreamMessage(UpstreamMessage message) {
        final String type = message.getDataPayload().get("type");
        final String payload = message.getDataPayload().get("payload");

        sendDownstreamMessage(MessageHelper.createJsonAck(message.getFrom(), message.getMessageId()));

        switch (type) {
            case "SAVE_IN_DATABASE":
                saveJsonBlobToDatabase(payload);
                break;
            case "FORWARD_TO_REST_SERVICE":
                RESTSender restSender = new RESTSender();
                restSender.sendMessage(message.getDataPayload().get("rest_endpoint"),
                        message.getDataPayload().get("rest_credentials"),
                        payload);
                break;
            default:
        }
    }


    private void saveJsonBlobToDatabase(String payload) {
        mBlobRepository.saveBlobToDatabase(new Blob(0l, null, payload));
    }

    public void handleAckReceipt(Map<String, Object> jsonMap) {
        // TODO
    }

    public void handleNackReceipt(Map<String, Object> jsonMap) {
        String errorCode = (String) jsonMap.get("error");

        if (errorCode == null) {
            return;
        }

        switch (errorCode) {
            case "INVALID_JSON":
                handleUnrecoverableFailure(jsonMap);
                break;
            case "BAD_REGISTRATION":
                handleUnrecoverableFailure(jsonMap);
                break;
            case "DEVICE_UNREGISTERED":
                handleUnrecoverableFailure(jsonMap);
                break;
            case "BAD_ACK":
                handleUnrecoverableFailure(jsonMap);
                break;
            case "SERVICE_UNAVAILABLE":
                handleServerFailure(jsonMap);
                break;
            case "INTERNAL_SERVER_ERROR":
                handleServerFailure(jsonMap);
                break;
            case "DEVICE_MESSAGE_RATE_EXCEEDED":
                handleUnrecoverableFailure(jsonMap);
                break;
            case "TOPICS_MESSAGE_RATE_EXCEEDED":
                handleUnrecoverableFailure(jsonMap);
                break;
            case "CONNECTION_DRAINING":
                handleConnectionDrainingFailure();
                break;
            default:
        }
    }

    public void sendDownstreamMessage(String jsonRequest) {
        Stanza request = new GcmPacketExtension(jsonRequest).toPacket();
        try {
            mConnection.sendStanza(request);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void handleDeliveryReceipt(Map<String, Object> jsonMap) {
        // TODO
    }

    public void handleControlMessage(Map<String, Object> jsonMap) {
        // TODO
        String controlType = (String) jsonMap.get("control_type");

        if (controlType.equals("CONNECTION_DRAINING")) {
            handleConnectionDrainingFailure();
        }
    }

    public void handleServerFailure(Map<String, Object> jsonMap) {
        // TODO
    }

    public void handleUnrecoverableFailure(Map<String, Object> jsonMap) {
        // TODO
    }

    public void handleConnectionDrainingFailure() {
        // TODO
    }
}
