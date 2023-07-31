package wtf.expensive.modules.impl.util;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;

@ModuleAnnotation(name = "ElytraFix", type = Type.UTIL)
public class ElytraFix extends Module {

    public static long delay;

    private final EventListener<EventUpdate> onUpdate = e -> {
        ItemStack stack = mc.player.inventory.getItemStack();
        if (stack.getItem() instanceof ItemArmor ia && System.currentTimeMillis() > delay) {
            if (ia.armorType == EntityEquipmentSlot.CHEST && mc.player.inventory.armorItemInSlot(2).getItem() == Items.ELYTRA) {
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                int nullSlot = findNullSlot();
                boolean needDrop = nullSlot == 999;
                if (needDrop) {
                    nullSlot = 9;
                }
                mc.playerController.windowClick(0, nullSlot, 1, ClickType.PICKUP, mc.player);
                if (needDrop) {
                    mc.playerController.windowClick(0, -999, 1, ClickType.PICKUP, mc.player);
                }
                delay = System.currentTimeMillis() + 300;
            }
        }
    };


    public static int findNullSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemAir) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return 999;
    }
}
