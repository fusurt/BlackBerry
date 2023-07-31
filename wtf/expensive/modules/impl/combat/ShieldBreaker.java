package wtf.expensive.modules.impl.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.utility.util.ChatUtility;
import wtf.expensive.utility.util.InventoryUtility;

@ModuleAnnotation(name = "Shield Breaker", type = Type.COMBAT)
public class ShieldBreaker extends Module {

    public static void breakShieldMethod(EntityLivingBase base, boolean setting) {
        if (InventoryUtility.doesHotbarHaveAxe() && setting) {
            int item = InventoryUtility.getAxe();
            if (base instanceof EntityPlayer && base.getActiveItemStack().getItem() instanceof ItemShield) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(item));
                mc.playerController.attackEntity(mc.player, base);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.resetCooldown();
                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
            }
        }
    }

}
