import se.kth.ledbat.LedbatReceiverComp;
import se.sics.kompics.Component;
import se.sics.kompics.Init;
import se.sics.ktoolbox.util.identifiable.basic.PairIdentifier;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import se.sics.kompics.ComponentDefinition;

public class Main extends ComponentDefinition implements Runnable {

    Socket sock;
    String[] args2;
    public Main() {

    }

    public Main(Socket sock) {
        this.sock = sock;
    }

    public static void main(String[] args) {
        args2 = args;
        new Main().doThings();
    }

    public void doThings() {

        /*
            arg[1] == server || client
            arg[2] == IP of server

         */

        int port = 8080;

        // Server
        if(args[1].equalsIgnoreCase("server")) {
            ServerSocket socket = new ServerSocket(port);
            while (true) {
                new Thread(new Main(socket.accept())).run(); // Start a new thread to handle the accepted connection
            }

        } else { // client
            InetAddress addr = InetAddress.getByName(args[2]);
            Component receiver = create(LedbatReceiverComp.class, Init.NONE);

            LedbatReceiverComp ledbatRecv = new LedbatReceiverComp(Init.NONE);
        }
    }

    @Override
    public void run() {

    }
}
