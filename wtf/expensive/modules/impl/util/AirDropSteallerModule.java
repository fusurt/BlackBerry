package wtf.expensive.modules.impl.util;

import net.minecraft.block.BlockNote;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Mouse;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.impl.combat.AutoTotem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleAnnotation(name = "AirDropStealer", desc = "ќчень быстро стилит вещи из аирдропа", type = Type.UTIL)
public class AirDropSteallerModule extends Module {

    private final EventListener<EventUpdate> onUpdate = eventUpdate -> {
        if (Mouse.isButtonDown(0) && mc.player.ticksExisted % 5 == 0) {
            BlockPos pos = getSphere(getPlayerPosLocal(), 6, 4, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> AutoTotem.getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
            if (pos == null) return;
            mc.playerController.clickBlock(pos, EnumFacing.UP);
        }

        if (mc.player.openContainer instanceof ContainerChest) {

            ContainerChest chest = (ContainerChest) mc.player.openContainer;

            StringBuilder builder = new StringBuilder();
            String s = chest.getLowerChestInventory().getDisplayName().getUnformattedText();
            char[] buffer = s.toCharArray();
            for (int w = 0; w < buffer.length; w++) {
                char c = buffer[w];
                if (c == 'І') {
                    w++;
                } else {
                    builder.append(c);
                }
            }

            String s1 = builder.toString();
            if (s1.contains("јирƒроп")) {

                for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {

                    ItemStack stack = chest.getLowerChestInventory().getStackInSlot(i);

                    if (!(stack.getItem() instanceof ItemAir)) {
                        click(chest.windowId, i, 10);
                    }

                }
            }
        }
    };

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockNote;
    }

    public static BlockPos getPlayerPosLocal() {
        if (mc.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        for (int n4 = x - (int) n; n4 <= x + n; ++n4) {
            for (int n5 = z - (int) n; n5 <= z + n; ++n5) {
                for (int n6 = b2 ? (y - (int) n) : y; n6 < (b2 ? (y + n) : ((float) (y + n2))); ++n6) {
                    final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }

    public static void click(int window, int slot, int multi) {
        for (int i = 0; i < multi; i++) {
            mc.playerController.windowClick(window, slot + i, 0, ClickType.QUICK_MOVE, mc.player);
        }
    }

}
