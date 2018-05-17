package se.kth.ledbat.Driver;

import se.kth.ledbat.Serialization.NetSerializer;
import se.sics.kompics.Kompics;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.util.ByteIdentifier;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    static {
        // register
        Serializers.register(new NetSerializer(), "netS");
        // map
        Serializers.register(BasicAddress.class, "netS");
        Serializers.register(BasicHeader.class, "netS");
        Serializers.register(BasicContentMsg.class, "netS");
    }

    public static void main(String[] args) {

        try {
            if (args.length == 2) { // TODO adapt the length to our needs

            } else if (args.length == 4) { // TODO adapt the length to our needs

            } else {
                System.err.println("Invalid number of parameters");
                System.exit(1);
            }
        } catch (UnknownHostException ex) {
            System.err.println(ex);
            System.exit(1);
        }




        try {
            InetAddress ip = InetAddress.getLocalHost();
            int port = Integer.parseInt(args[0]);
            BasicAddress self = new BasicAddress(ip, port,
                    new PairIdentifier<ByteIdentifier, ByteIdentifier>(new ByteIdentifier((byte) 1), new ByteIdentifier((byte) 2)));
            Kompics.createAndStart(ReceiverParent.class, new ReceiverParent.Init(self), 2);


        } catch (UnknownHostException ex) {
            System.err.println(ex);
            System.exit(1);
        }


        Kompics.createAndStart(ReceiverParent.class);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            System.exit((1));
        }
        Kompics.shutdown();
        System.exit(0);
    }
}
