package wtf.expensive.modules.impl.combat;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "Velocity", type = Type.COMBAT)
public class VelocityModule extends Module {

    public final EventListener<EventPacket> onPacketReceiveEvent = e -> {
        if (e.getPacketType() == EventPacket.PacketType.RECEIVE) {
            if (e.getPacket() instanceof SPacketEntityVelocity) {
                SPacketEntityVelocity packet = (SPacketEntityVelocity) e.getPacket();
                if (packet.getEntityID() == mc.player.getEntityId()) {
                    e.cancel();
                }
            }
            if (e.getPacket() instanceof SPacketExplosion) {
                e.cancel();
            }
        }
    };

}
