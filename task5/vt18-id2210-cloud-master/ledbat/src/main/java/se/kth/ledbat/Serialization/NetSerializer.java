package se.kth.ledbat.Serialization;

import com.google.common.base.Optional;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ByteProcessor;
import se.kth.ledbat.Driver.MyIdentifiable;
import se.kth.ledbat.Driver.MyString;
import se.kth.ledbat.Ledbat;
import se.kth.ledbat.msgs.LedbatMsg;
import se.kth.ledbat.util.OneWayDelay;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.netty.serialization.Serializer;
import se.sics.kompics.network.netty.serialization.Serializers;
import se.sics.kompics.util.Identifier;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;

// This implementation is heavliy inspired by the Kompics tutorial "Basic Networking"
public class NetSerializer implements Serializer {

    private static final byte ADDR = 1;
    private static final byte HEADER = 2;
    private static final byte DATAMSG = 3;
    private static final byte ACKMSG = 4;
    private static final byte ONEWAYDELAY = 5;
    private static final byte MYSTRING = 6;
    private static final byte MYIDENTIFIABLE = 7;
    private static final byte DATA = 8;
    private static final byte ACK = 9;

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
            this.toBinary(addr.getId(), buf);

        } else if (o instanceof BasicHeader) {
            BasicHeader header = (BasicHeader) o;
            buf.writeByte(HEADER); // mark which type we are serializing (1 byte)
            this.toBinary(header.getSource(), buf); // use this serializer again (7 bytes)
            this.toBinary(header.getDestination(), buf); // use this serializer again (7 bytes)
            buf.writeByte(header.getProtocol().ordinal()); // 1 byte is enough
            // total = 16 bytes
        } else if (o instanceof BasicContentMsg) {
            BasicContentMsg msg = (BasicContentMsg) o;

            if (msg.getContent() instanceof LedbatMsg.Data) {
                buf.writeByte(DATAMSG);
                this.toBinary(msg.getHeader(), buf);
                this.toBinary(msg.getContent(), buf);
            } else if (msg.getContent() instanceof LedbatMsg.Ack) {
                buf.writeByte(ACKMSG);
                this.toBinary(msg.getHeader(), buf);
                this.toBinary(msg.getContent(), buf);
            }

        } else if (o instanceof OneWayDelay) {
            OneWayDelay oneWayDelay = (OneWayDelay) o;
            buf.writeByte(ONEWAYDELAY);
            buf.writeLong(oneWayDelay.send);
            buf.writeLong(oneWayDelay.receive);
            // Total 1+8+8 = 17 bytes
        } else if (o instanceof MyString) {
            MyString myString = (MyString) o;
            buf.writeByte(MYSTRING);
            // buf.writeBytes(myString.getId().getBytes(Charset.forName("UTF-16")));
            // Total = depends on string length

        } else if (o instanceof MyIdentifiable) {
            MyIdentifiable myIdentifiable = (MyIdentifiable) o;
            buf.writeByte(MYIDENTIFIABLE);
            this.toBinary(myIdentifiable.getId(), buf);
            // Total = depends on string length

        } else if (o instanceof LedbatMsg.Data) {
            LedbatMsg.Data data = (LedbatMsg.Data) o;
            buf.writeByte(DATA);
            this.toBinary(data.dataId, buf);
            this.toBinary(data.data, buf);
            this.toBinary(data.dataDelay, buf);
        } else if (o instanceof LedbatMsg.Ack) {
            LedbatMsg.Ack ack = (LedbatMsg.Ack) o;
            buf.writeByte(ACK);
            this.toBinary(ack.eventId, buf);
            this.toBinary(ack.dataId, buf);
            this.toBinary(ack.dataDelay, buf);
            this.toBinary(ack.ackDelay, buf);
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
                    MyString myString = (MyString) this.fromBinary(buf, Optional.absent());
                    return new BasicAddress(ip, port, myString);
                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex); // Let Netty deal with this
                }
            }
            case HEADER: {
                BasicAddress src = (BasicAddress) this.fromBinary(buf, Optional.absent()); // We already know what it's going to be (7 bytes)
                BasicAddress dst = (BasicAddress) this.fromBinary(buf, Optional.absent()); // same here (7 bytes)
                int protoOrd = buf.readByte(); // 1 byte
                Transport proto = Transport.values()[protoOrd];
                return new BasicHeader<>(src, dst, proto); // Total = 16 bytes, check
            }
            case DATAMSG: {
                BasicHeader header = (BasicHeader) this.fromBinary(buf, Optional.absent());
                LedbatMsg.Data data = (LedbatMsg.Data) this.fromBinary(buf, Optional.absent());
                return new BasicContentMsg(header, data);
            }
            case ACKMSG: {
                BasicHeader header = (BasicHeader) this.fromBinary(buf, Optional.absent());
                LedbatMsg.Ack ack = (LedbatMsg.Ack) this.fromBinary(buf, Optional.absent());
                return new BasicContentMsg(header, ack);
            }
            case ONEWAYDELAY: { // TODO maybe not supposed to assign values?
                long send = buf.readLong();
                long receive = buf.readLong();
                OneWayDelay oneWayDelay = new OneWayDelay();
                oneWayDelay.send(send);
                oneWayDelay.receive(receive);
                return oneWayDelay;
                // Total = 17 bytes
            }
            case MYSTRING: {
                // return new MyString(buf.toString(Charset.forName("UTF-16")));
                return new MyString(buf.toString(Charset.forName("UTF-16")));
            }
            case MYIDENTIFIABLE: {
                MyString kekeke = (MyString) this.fromBinary(buf, Optional.absent());
                return new MyIdentifiable(kekeke.getId());
            }
            case DATA: {
                MyString myString = (MyString) this.fromBinary(buf, Optional.absent());
                MyIdentifiable content = (MyIdentifiable) this.fromBinary(buf, Optional.absent());
                OneWayDelay delay = (OneWayDelay) this.fromBinary(buf, Optional.absent()); // TODO we don't use this
                return new LedbatMsg.Data(myString, content);
            }
            case ACK: {
                MyString eventId = (MyString) this.fromBinary(buf, Optional.absent());
                MyString dataId = (MyString) this.fromBinary(buf, Optional.absent());
                OneWayDelay dataDelay = (OneWayDelay) this.fromBinary(buf, Optional.absent());
                OneWayDelay ackDelay = (OneWayDelay) this.fromBinary(buf, Optional.absent()); // TODO we don't use this
                return new LedbatMsg.Ack(eventId, dataId, dataDelay);
            }
        }
        return null; // Strange things happened
    }
}
