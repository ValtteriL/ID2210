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

public class SenderParent extends ComponentDefinition {

    public SenderParent(Init init) {
        Component sender = create(LedbatSenderComp.class, Init.NONE);
        Component timer = create(JavaTimer.class, Init.NONE);
        Component network = create(NettyNetwork.class, new NettyInit(init.self));

        connect(sender.getNegative(Timer.class), timer.getPositive(Timer.class), Channel.TWO_WAY);
        connect(sender.getNegative(Network.class), network.getPositive(Network.class), Channel.TWO_WAY);
    }

    public static class Init extends se.sics.kompics.Init<SenderParent> {
        // TODO what do we need in this Init?
        public BasicAddress self;
        public Init(BasicAddress self) {
            this.self = self;
        }
    }

}
