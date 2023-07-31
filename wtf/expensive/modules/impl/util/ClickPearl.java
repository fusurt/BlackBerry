package wtf.expensive.modules.impl.util;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.network.EventPacket;
import wtf.expensive.event.impl.player.EventMouseTick;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AuraModule;
import wtf.expensive.utility.util.InventoryUtility;

@ModuleAnnotation(name = "ClickPearl", type = Type.UTIL)
public class ClickPearl extends Module {
    ItemStack itemStack;
    private final EventListener<EventPacket> onPacket = e -> {
//        if (e.getPacketType() == EventPacket.PacketType.SEND) {
//            if (e.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
//                if (mc.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL) {
//                    e.cancel();
//                }
//            }
    };
    private final EventListener<EventMouseTick> onMouse = e -> {

        if (e.getButton() == 2 && !mc.player.getCooldownTracker().hasCooldown(Items.ENDER_PEARL) && InventoryUtility.getPearls() >= 0) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtility.getPearls()));

            if (AuraModule.instance.target != null && Expensive.getInstance().getModuleManager().get(AuraModule.class).state) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, true));
            }

            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));

        }
    };
}
