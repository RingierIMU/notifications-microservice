import org.jivesoftware.smack.XMPPException;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import utils.Util;
import xmpp.CloudConnection;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        Logger.info("Starting XMPP server in the background");
        new XmppServerThread().run();
        super.onStart(app);
    }

    class XmppServerThread extends Thread {
        public void run() {
            try {
                CloudConnection.prepareClient(Util.FCM_SENDERID, Util.FCM_SERVER_KEY);
                CloudConnection cloudConnection = CloudConnection.getInstance();
                cloudConnection.connectToCloud();
            } catch (XMPPException xmppexception) {
                xmppexception.printStackTrace();
            } catch (IllegalStateException illegalStateException) {
                illegalStateException.printStackTrace();
            }
        }
    }
}