package se.kth.ledbat.Driver;

import se.kth.ledbat.Ledbat;
import se.kth.ledbat.Serialization.NetSerializer;
import se.kth.ledbat.msgs.LedbatMsg;
import se.kth.ledbat.util.Cwnd;
import se.kth.ledbat.util.LedbatConfig;
import se.kth.ledbat.util.OneWayDelay;
import se.kth.ledbat.util.RTTEstimator;
import se.sics.kompics.Init;
import se.sics.kompics.Kompics;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.network.netty.serialization.JavaSerializer;
import se.sics.kompics.util.ByteIdentifier;
import se.sics.kompics.util.Identifiable;
import se.sics.kompics.util.Identifier;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;
import io.netty.handler.codec.serialization.ClassResolvers;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class Main {

    static {
        // register
        Serializers.register(new NetSerializer(), "netS");

        // map
        Serializers.register(BasicAddress.class, "netS");
        Serializers.register(BasicContentMsg.class, "netS");
        Serializers.register(BasicHeader.class, "netS");
        Serializers.register(MyIdentifiable.class, "netS");
        Serializers.register(MyString.class, "netS");
//        Serializers.register(LedbatMsg.class, "netS");
        Serializers.register(LedbatMsg.Ack.class, "netS");
//        Serializers.register(LedbatMsg.BulkAck.class, "netS");
        Serializers.register(LedbatMsg.Data.class, "netS");
        Serializers.register(OneWayDelay.class, "netS");
    }


    public static void main(String[] args) {

        try {

            if (args.length == 0) { // Server

                Kompics.createAndStart(ReceiverParent.class, Init.NONE, 2);
                System.out.println("Started ReceiverParent...");
            } else if (args.length == 1) { // Client

                Kompics.createAndStart(SenderParent.class, Init.NONE, 2);
                System.out.println("Started SenderParent...");
            } else {
                System.err.println("Invalid number of parameters");
                System.exit(1);
            }

            Thread.sleep(15000);
            Kompics.shutdown();
            System.exit(0);
        } catch (InterruptedException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
