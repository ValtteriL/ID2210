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

public class ReceiverParent extends ComponentDefinition {

    public ReceiverParent(Init init) {
        Component receiver = create(LedbatReceiverComp.class,
                new LedbatReceiverComp.Init(new Main.MyIdentifier("c"), new Main.MyIdentifier("b"), new Main.MyIdentifier("a")));
        Component network = create(NettyNetwork.class, new NettyInit(init.self));

        connect(receiver.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
    }

    public static class Init extends se.sics.kompics.Init<ReceiverParent> {
        public BasicAddress self;
        public Init(BasicAddress self) {
            this.self = self;
        }
    }

}
