package se.kth.ledbat.Driver;

import se.kth.ledbat.LedbatReceiverComp;
import se.kth.ledbat.LedbatSenderComp;
import se.sics.kompics.Channel;
import se.sics.kompics.Component;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.netty.NettyInit;
import se.sics.kompics.network.netty.NettyNetwork;
import se.sics.kompics.timer.Timer;
import se.sics.kompics.timer.java.JavaTimer;
import se.sics.ktoolbox.util.network.basic.BasicAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SenderParent extends ComponentDefinition {

    public SenderParent() {
        try {
            InetAddress ip = InetAddress.getByName(config().getValue("ledbat.self.host", String.class));
            int port = config().getValue("ledbat.self.port1", Integer.class);

            BasicAddress basicAddr = new BasicAddress(ip, port, new Main.MyIdentifier("Something"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Component sender = create(LedbatSenderComp.class,
                new LedbatSenderComp.Init(new Main.MyIdentifier("a"), new Main.MyIdentifier("a"), new Main.MyIdentifier("a")));
        Component timer = create(JavaTimer.class, Init.NONE);
        Component network = create(NettyNetwork.class, new NettyInit(basicAddr));

        connect(sender.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
        connect(sender.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
    }
}
