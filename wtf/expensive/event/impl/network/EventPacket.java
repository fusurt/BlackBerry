package wtf.expensive.event.impl.network;

import net.minecraft.network.Packet;
import wtf.expensive.event.Event;

public class EventPacket extends Event {

    private Packet packet;

    private final PacketType packetType;

    public EventPacket(Packet packet, PacketType packetType) {
        this.packet = packet;
        this.packetType = packetType;
    }

    public Packet getPacket() {
        return packet;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public enum PacketType {
        SEND, RECEIVE
    }
}
