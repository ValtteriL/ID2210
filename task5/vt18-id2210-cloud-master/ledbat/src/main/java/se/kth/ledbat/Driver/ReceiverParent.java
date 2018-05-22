package se.kth.ledbat.Driver;

import se.kth.ledbat.LedbatReceiverComp;
import se.kth.ledbat.LedbatSenderComp;
import se.sics.kompics.Channel;
import se.sics.kompics.Init;
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

public class ReceiverParent extends ComponentDefinition {

    public ReceiverParent() {

        BasicAddress basicAddr = null;
        BasicAddress basicAddr2 = null;
        try {
            InetAddress ip = InetAddress.getByName(config().getValue("ledbat.self.host", String.class));
            int port = config().getValue("ledbat.self.port2", Integer.class);

            basicAddr = new BasicAddress(ip, port, new Main.MyIdentifier("a"));
            basicAddr2 = new BasicAddress(ip, port+1, new Main.MyIdentifier("b"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // create networks and timer
        Component network1 = create(NettyNetwork.class, new NettyInit(basicAddr));
        Component network2 = create(NettyNetwork.class, new NettyInit(basicAddr2));
        Component timer = create(JavaTimer.class, Init.NONE);

        // create receiver
        Component receiver = create(LedbatReceiverComp.class,
                new LedbatReceiverComp.Init(new Main.MyIdentifier("a"), new Main.MyIdentifier("b"), new Main.MyIdentifier("c")));

        // create sender
        Component sender = create(LedbatSenderComp.class,
            new LedbatSenderComp.Init(new Main.MyIdentifier("a"), new Main.MyIdentifier("b"), new Main.MyIdentifier("c")));

        // connect timer and network1 to sender
        connect(sender.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY); // connect timer to sender
        connect(sender.getNegative(Network.class), network1.getPositive(Network.class), Channel.TWO_WAY); // connect sender to network

        // connect network 2 to receiver
        connect(receiver.getNegative(Network.class), network2.getPositive(Network.class), Channel.TWO_WAY); // connect receiver to network
    }
}
