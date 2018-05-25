package se.kth.ledbat.ApplicationLayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ledbat.Driver.MyIdentifiable;
import se.kth.ledbat.Driver.MyIdentifier;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Sender extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);
    private String logPrefix = "";

    Positive<Network> ledbatSender = requires(Network.class);

    public Sender() {
        subscribe(handleStart, control);
    }

    Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            LOG.info("{}starting...", logPrefix);

            while (true) {
                try {
                    Thread.sleep(2000);
                    BasicContentMsg msg = new BasicContentMsg(new BasicHeader(
                            new BasicAddress(InetAddress.getByName("127.0.0.1"), 8080, new MyIdentifier("senderID")),
                            new BasicAddress(InetAddress.getByName("127.0.0.1"), 8081, new MyIdentifier("receiverID")),
                            Transport.UDP),
                            new MyIdentifiable("this is a message"));
                    trigger(msg, ledbatSender);
                    //LOG.debug("Sender sending {}", msg.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
    };


}
