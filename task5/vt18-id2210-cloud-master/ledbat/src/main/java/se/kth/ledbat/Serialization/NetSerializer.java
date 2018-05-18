package se.kth.ledbat.Serialization;

import com.google.common.base.Optional;
import io.netty.buffer.ByteBuf;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.netty.serialization.Serializer;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.net.InetAddress;
import java.net.UnknownHostException;

// This implementation is heavliy inspired by the Kompics tutorial "Basic Networking"
public class NetSerializer implements Serializer {

    private static final byte ADDR = 1;
    private static final byte HEADER = 2;
    private static final byte MSG = 3;

    @Override
    public int identifier() {
        return 100;
    }

    @Override
    public void toBinary(Object o, ByteBuf buf) {
        if (o instanceof BasicAddress) {
            BasicAddress addr = (BasicAddress) o;
            buf.writeByte(ADDR); // mark which type we are serializing (1 byte)
            buf.writeBytes(addr.getIp().getAddress()); // 4 bytes IP
            buf.writeShort(addr.getPort()); // We only need 2 bytes here
            // total = 7 bytes

        } else if (o instanceof BasicHeader) {
            BasicHeader header = (BasicHeader) o;
            buf.writeByte(HEADER); // mark which type we are serializing (1 byte)
            this.toBinary(header.getSource(), buf); // use this serializer again (7 bytes)
            this.toBinary(header.getDestination(), buf); // use this serializer again (7 bytes)
            buf.writeByte(header.getProtocol().ordinal()); // 1 byte is enough
            // total = 16 bytes
        } else if (o instanceof BasicContentMsg) {
            // TODO fill in


        }
    }

    @Override
    public Object fromBinary(ByteBuf buf, Optional<Object> hint) {
        byte type = buf.readByte(); // read the first byte to figure out the type
        switch (type) {
            case ADDR: {
                byte[] ipBytes = new byte[4];
                buf.readBytes(ipBytes);
                try {
                    InetAddress ip = InetAddress.getByAddress(ipBytes); // 4 bytes
                    int port = buf.readUnsignedShort(); // 2 bytes
                    return new BasicAddress(ip, port, null); // TODO fix serialization for the identifier
                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex); // Let Netty deal with this
                }
            }
            case HEADER: {
                // TODO fix these. They will not be 7 bytes after we add serialization for the Identifier in BasicAddress
                BasicAddress src = (BasicAddress) this.fromBinary(buf, Optional.absent()); // We already know what it's going to be (7 bytes)
                BasicAddress dst = (BasicAddress) this.fromBinary(buf, Optional.absent()); // same here (7 bytes)
                int protoOrd = buf.readByte(); // 1 byte
                Transport proto = Transport.values()[protoOrd];
                return new BasicHeader<>(src, dst, proto); // Total = 16 bytes, check
            }
            case MSG: {
                // TODO fill in


            }
        }
        return null; // Strange things happened
    }
}
