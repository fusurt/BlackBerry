package wtf.expensive.modules.impl.util;

import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.player.NoInteractModule;

@ModuleAnnotation(name = "Anti Throw Block", type = Type.UTIL)
public class AntiThrowBlock extends Module {
    public static int ticks;
    private final EventListener<EventPacket> onMouse = e -> {

        if (e.getPacketType() == EventPacket.PacketType.SEND) {
            if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock packet) {
                if (mc.player.getHeldItem(packet.getHand()).getItem() == Items.ENDER_PEARL) {

                    if (!Expensive.getInstance().getModuleManager().get(NoInteractModule.class).state) {
                        Expensive.getInstance().getModuleManager().get(NoInteractModule.class).setState(true);
                    }
                    ticks = 10;

                    e.setCancelled(true);

                }

            }
        }
        if (ticks > 0) {
            ticks--;
        }
    };
}
