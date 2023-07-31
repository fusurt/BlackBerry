package wtf.expensive.modules.impl.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.Expensive;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.event.impl.render.EventDraw;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.MultiBoxSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.font.Fonts;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ModuleAnnotation(name = "AutoTotem", type = Type.COMBAT)
public class AutoTotem extends Module {

    public SliderSetting health = new SliderSetting("Health", 3.5f, 1.f, 20.f, 0.05f);
    BooleanSetting noBallSwitch = new BooleanSetting("No Ball Switch", false);

    public MultiBoxSetting mode = new MultiBoxSetting("Addons", new String[]{"Check Absorption", "Check Explosion",
            "Check Obsidian", "Check Fall"});
    SliderSetting radiusExplosion = new SliderSetting("Distance to Explosion", 6, 1, 8, 1, () -> mode.get(1));
    SliderSetting radiusObs = new SliderSetting("Distance to Obsidian", 6, 1, 8, 1, () -> mode.get(2));
    BooleanSetting counter = new BooleanSetting("Counter", true);
    public int swapBack = -1;
    public long delay = 0;
    private final EventListener<EventUpdate> onUpdate = e -> {

        int slot = getSlotIDFromItem(Items.TOTEM_OF_UNDYING);

        boolean totemInHand = mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        boolean handNotNull = !(mc.player.getHeldItemOffhand().getItem() instanceof ItemAir);

        if (System.currentTimeMillis() < delay) {
            return;
        }

        if (condition()) {
            if (slot >= 0) {
                if (!totemInHand) {
                    mc.playerController.windowClick(0, slot, 1, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
                    if (handNotNull) {
                        mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                        if (swapBack == -1) swapBack = slot;
                    }
                    delay = System.currentTimeMillis() + 300;

                }
            }

        } else if (swapBack >= 0) {
            mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            if (handNotNull) mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
            swapBack = -1;
            delay = System.currentTimeMillis() + 300;
        }

    };

    private final EventListener<EventDraw> onRender = event -> {
        if (event.type == EventDraw.RenderType.DISPLAY) {
            if (counter.get()) {
                Expensive.getInstance().getScaleMath().pushScale();
                if (fountTotemCount() > 0) {
                    Fonts.MONTSERRAT12.drawCenteredString(fountTotemCount() + "", event.sr.getScaledWidth() / 2,
                            event.sr.getScaledHeight() / 2 + 40, new Color(255, 255, 255).getRGB());
                    for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
                        ItemStack stack = mc.player.inventory.getStackInSlot(i);
                        if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                            GlStateManager.pushMatrix();
                            GlStateManager.disableBlend();
                            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.sr.getScaledWidth() / 2 - 8, event.sr.getScaledHeight() / 2 + 20);
                            GlStateManager.popMatrix();
                        }
                    }
                }
                Expensive.getInstance().getScaleMath().popScale();
            }
        }
    };

    private boolean condition() {

        float hp = mc.player.getHealth();
        if (mode.get(0)) {
            hp += mc.player.getAbsorptionAmount();
        }

        if (health.get() >= hp) {
            return true;
        }

        if (!isBall()) {
            if (mode.get(1)) {
                for (Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal && mc.player.getDistanceToEntity(entity) <= radiusExplosion.get()) {
                        return true;
                    }
                    if (entity instanceof EntityTNTPrimed && mc.player.getDistanceToEntity(entity) <= radiusExplosion.get()) {
                        return true;
                    }
                    if (entity instanceof EntityMinecartTNT && mc.player.getDistanceToEntity(entity) <= radiusExplosion.get()) {
                        return true;
                    }
                }
            }

            if (mode.get(2)) {
                BlockPos pos = getSphere(getPlayerPosLocal(), radiusObs.get(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
                return pos != null;
            }
            if (mode.get(3)) {
                return mc.player.fallDistance >= 30;
            }
        }

        return false;
    }

    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < mc.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                count++;
            }
        }
        return count;
    }

    public boolean isBall() {

        if (!noBallSwitch.get()) {
            return false;
        }

        ItemStack stack = mc.player.getHeldItemOffhand();
        return stack.getDisplayName().toLowerCase().contains("шар") || stack.getDisplayName().toLowerCase().contains("голова");
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

    public BlockPos getPlayerPosLocal() {
        if (mc.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        return state.getBlock() instanceof BlockObsidian;
    }

    public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static int getSlotIDFromItem(Item item) {
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.inventory.getStackInSlot(i);
            if (s.getItem() == item) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }
        return slot;
    }

    public static double getDistance(final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        final double n7 = n - n4;
        final double n8 = n2 - n5;
        final double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }

}
