package xmpp;

import models.UpstreamMessage;
import play.libs.Json;

import java.util.HashMap;
import java.util.Map;

public class MessageHelper {

    public static String createJsonAck(String to, String messageId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message_type", "ack");
        map.put("to", to);
        map.put("message_id", messageId);
        return Json.toJson(map).toString();
    }

    public static UpstreamMessage createUpstreamMessage(Map<String, Object> jsonMap) {
        String from = jsonMap.get("from").toString();
        String category = jsonMap.get("category").toString();
        String messageId = jsonMap.get("message_id").toString();
        Map<String, String> dataPayload = (Map<String, String>) jsonMap.get("data");
        return new UpstreamMessage(from, category, messageId, dataPayload);
    }

}