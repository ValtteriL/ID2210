package se.kth.ledbat.Driver;

import se.kth.ledbat.LedbatReceiverComp;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.timer.java.JavaTimer;
import se.sics.ktoolbox.util.network.basic.BasicAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ReceiverParent extends ComponentDefinition {

    public ReceiverParent() {

        BasicAddress basicAddr = null;
        try {

            InetAddress ip = InetAddress.getByName(config().getValue("ledbat.self.host", String.class));
            int port = config().getValue("ledbat.self.port2", Integer.class);


            // Hardcoded stuff
            //InetAddress ip = InetAddress.getByName("0.0.0.0");
            //int port = 8081;

            basicAddr = new BasicAddress(ip, port, new MyIdentifier("receiverID"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Component receiver = create(LedbatReceiverComp.class,
                new LedbatReceiverComp.Init(
                        new MyIdentifier("dataID"), // dataID
                        new MyIdentifier("senderID"), // senderID
                        new MyIdentifier("receiverID"))); // receiverID
        Component network = create(NettyNetwork.class, new NettyInit(basicAddr));

        connect(receiver.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
    }
}
