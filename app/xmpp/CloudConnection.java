package xmpp;

import models.UpstreamMessage;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Element;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import play.libs.Json;
import utils.MessageHandler;
import utils.Util;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.Map;

public class CloudConnection implements StanzaListener {
    private static CloudConnection mInstance = null;
    private MessageHandler mMessageHandler;
    private XMPPTCPConnection mConnection;
    private String mApiKey = null;
    private String mProjectId = null;

    private CloudConnection(String projectId, String apiKey) {
        this();
        mApiKey = apiKey;
        mProjectId = projectId;
    }

    private CloudConnection() {
        ProviderManager.addExtensionProvider(Util.FCM_ELEMENT_NAME, Util.FCM_NAMESPACE,
                new ExtensionElementProvider() {

                    @Override
                    public Element parse(XmlPullParser parser, int initialDepth)
                            throws XmlPullParserException, IOException, SmackException {
                        String json = parser.nextText();
                        return new GcmPacketExtension(json);
                    }
                });
    }

    public static CloudConnection getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Client unprepared");
        }
        return mInstance;
    }

    public static CloudConnection prepareClient(String projectId, String apiKey) {
        synchronized (CloudConnection.class) {
            if (mInstance == null) {
                mInstance = new CloudConnection(projectId, apiKey);
            }
        }
        return mInstance;
    }

    public void connectToCloud() throws XMPPException {
        XMPPTCPConnectionConfiguration connectionConfig = XMPPTCPConnectionConfiguration.builder()
                .setDebuggerEnabled(true)
                .setServiceName(Util.FCM_SERVER_CONNECTION)
                .setUsernameAndPassword(mProjectId, mApiKey)
                .setHost(Util.FCM_SERVER)
                .setPort(Util.FCM_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
                .setSendPresence(false)
                .setSocketFactory(SSLSocketFactory.getDefault())
                .build();

        mConnection = new XMPPTCPConnection(connectionConfig);

        mConnection.addAsyncStanzaListener(this, new StanzaTypeFilter(Message.class));

        try {
            mConnection.connect();
            mConnection.login();
            mMessageHandler = new MessageHandler(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
        Message incomingMessage = (Message) packet;
        GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage.getExtension(Util.FCM_NAMESPACE);
        String json = gcmPacket.getJson();

        Map<String, Object> jsonMap = (Map<String, Object>) Json.fromJson((Json.parse(json)), Map.class);
        Object messageType = jsonMap.get("message_type");

        if (messageType == null) {
            UpstreamMessage upstreamMessage = MessageHelper.createUpstreamMessage(jsonMap);
            mMessageHandler.handleUpstreamMessage(upstreamMessage);
        } else {
            switch (messageType.toString()) {
                case "ack":
                    mMessageHandler.handleAckReceipt(jsonMap);
                    break;
                case "nack":
                    mMessageHandler.handleNackReceipt(jsonMap);
                    break;
                case "receipt":
                    mMessageHandler.handleDeliveryReceipt(jsonMap);
                    break;
                case "control":
                    mMessageHandler.handleControlMessage(jsonMap);
                    break;
                default:
            }
        }
    }

}

