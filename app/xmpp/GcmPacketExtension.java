package xmpp;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import utils.Util;

public class GcmPacketExtension extends DefaultExtensionElement {

    private String json;

    public GcmPacketExtension(String json) {
        super(Util.FCM_ELEMENT_NAME, Util.FCM_NAMESPACE);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toXML() {
        // TODO: Do we need to scape the json? StringUtils.escapeForXML(json)
        return String.format("<%s xmlns=\"%s\">%s</%s>", Util.FCM_ELEMENT_NAME, Util.FCM_NAMESPACE, json,
                Util.FCM_ELEMENT_NAME);
    }

    public Stanza toPacket() {
        Message message = new Message();
        message.addExtension(this);
        return message;
    }
}
