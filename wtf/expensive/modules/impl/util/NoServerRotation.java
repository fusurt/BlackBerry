package wtf.expensive.modules.impl.util;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "No Server Rotation", type = Type.UTIL)
public class NoServerRotation extends Module {
    boolean sentOne;
    float yaw,pitch;
    private final EventListener<EventPacket> onPacket = event -> {
        if (event.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (mc.player == null) {
                return;
            }
            if (event.getPacket() instanceof SPacketPlayerPosLook packet) {
                sentOne = Math.abs(yaw - packet.yaw) > 0 || Math.abs(pitch - packet.pitch) > 0;
                yaw = packet.yaw;
                pitch = packet.pitch;
                packet.yaw = mc.player.rotationYaw;
                packet.pitch = mc.player.rotationPitch;
            }
            if (event.getPacket() instanceof CPacketPlayer packet) {
                if (packet instanceof CPacketPlayer.Rotation packet2) {
                    packet2.yaw = yaw;
                    packet2.pitch = pitch;
                    sentOne = false;
                }
                if (packet instanceof CPacketPlayer.PositionRotation packet2) {
                    packet2.yaw = yaw;
                    packet2.pitch = pitch;
                    sentOne = false;
                }
            }
        }
    };
}
