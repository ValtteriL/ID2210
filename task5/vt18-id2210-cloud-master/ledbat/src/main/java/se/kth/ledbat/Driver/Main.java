package se.kth.ledbat.Driver;

import se.kth.ledbat.Serialization.NetSerializer;
import se.sics.kompics.Kompics;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.util.ByteIdentifier;
import se.sics.kompics.util.Identifier;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        try {

            if (args.length == 0) { // Server

                Kompics.createAndStart(ReceiverParent.class, Init.NONE, 2);
                System.out.println("Started ReceiverParent...");
            } else if (args.length == 1) { // Client

                Kompics.createAndStart(SenderParent.class, Init.NONE);
                System.out.println("Started SenderParent...");
            } else {
                System.err.println("Invalid number of parameters");
                System.exit(1);
            }

            Thread.sleep(15000);
            Kompics.shutdown();
            System.exit(0);
        } catch (UnknownHostException ex) {
            System.err.println(ex);
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println(e);
            System.exit(1);
        }
    }


    public static class MyIdentifier implements Identifier {

        String id;

        public MyIdentifier(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyIdentifier that = (MyIdentifier) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id);
        }

        @Override
        public int partition(int i) {
            return 0;
        }

        @Override
        public int compareTo(Identifier o) {
            return 0;
        }
    }

}
