package se.kth.ledbat.Driver;

import se.kth.ledbat.ApplicationLayer.Sender;
import se.kth.ledbat.LedbatReceiverComp;
import se.kth.ledbat.LedbatSenderComp;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Init;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import sun.nio.ch.Net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SenderParent extends ComponentDefinition {

    public SenderParent() {
        BasicAddress basicAddr = null;
        try {
            InetAddress ip = InetAddress.getByName(config().getValue("ledbat.self.host", String.class));
            int port = config().getValue("ledbat.self.port1", Integer.class);

            basicAddr = new BasicAddress(ip, port, new Main.MyIdentifier("sender"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Component ledbatSender = create(LedbatSenderComp.class,
                new LedbatSenderComp.Init(
                        new Main.MyIdentifier("data"), // dataID
                        new Main.MyIdentifier("sender"), // senderID
                        new Main.MyIdentifier("receiver"))); // receiverID
        Component timer = create(JavaTimer.class, Init.NONE);
        Component network = create(NettyNetwork.class, new NettyInit(basicAddr));
        Component sender = create(Sender.class, Init.NONE);

        connect(timer.getPositive(Timer.class), ledbatSender.getNegative(Timer.class), Channel.TWO_WAY);
        connect(ledbatSender.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
        connect(sender.getNegative(Network.class), ledbatSender.getPositive(Network.class), Channel.TWO_WAY);
    }
}
