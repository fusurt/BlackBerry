package wtf.expensive.utility.util;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import wtf.expensive.utility.Utility;

public class InventoryUtility implements Utility {
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }
    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }
    public static int getFireWorks() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemFirework) {
                return i;
            }
        }
        return -1;
    }
    public static int getPearls() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemEnderPearl) {
                return i;
            }
        }
        return -1;
    }

}
