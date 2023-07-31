package wtf.expensive.modules.impl.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import wtf.expensive.event.EventListener;
import wtf.expensive.event.impl.player.EventUpdate;
import wtf.expensive.modules.Module;
import wtf.expensive.modules.ModuleAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.BooleanSetting;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.utility.movement.MoveUtility;

import java.util.ArrayList;
import java.util.List;

@ModuleAnnotation(name = "WaterSpeed", type = Type.MOVEMENT)
public class WaterSpeedModule extends Module {

    public BooleanSetting speedCheck = new BooleanSetting("Speed Potion Check", false);
    public BooleanSetting smart = new BooleanSetting("Smart", false);
    public SliderSetting speed = new SliderSetting("Speed", 0.4f, 0.1f, 1f, 0.01f, () -> !smart.get());

    public BooleanSetting miniJump = new BooleanSetting("Mini Jump", true);
    public static float tick = 0;

    private final EventListener<EventUpdate> onUpdate = e -> {


        if (!smart.get()) {
            if (mc.player.isCollidedHorizontally || !mc.player.isInWater()
                    || speedCheck.get() && !mc.player.isPotionActive(MobEffects.SPEED)) {

                return;
            }
            MoveUtility.setMotion(speed.get());
        } else {
            List<ItemStack> stacks = new ArrayList<>();
            mc.player.getArmorInventoryList().forEach(stacks::add);
            stacks.removeIf(w -> w.getItem() instanceof ItemAir);
            float motion = MoveUtility.getMotion();
            boolean hasEnchantments = false;
            for (ItemStack stack : stacks) {

                int enchantmentLevel = 0;

                if (buildEnchantments(stack, 1)) {
                    enchantmentLevel = 1;
                }

                if (enchantmentLevel > 0) {
                    motion = 0.5f;
                    hasEnchantments = true;
                }
            }

            if (mc.player.isCollidedHorizontally) {
                tick = 0;
                return;
            }
            if (!mc.player.isInWater()) return;
            if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isSneaking()
                    && !(mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir)) {
                mc.player.motionY = 0.12f;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.35f;
            }
            if (speedCheck.get() && !mc.player.isPotionActive(MobEffects.SPEED)) {
                tick = 0;
                return;
            }


            if (miniJump.get() && hasEnchantments && mc.world.getBlockState(mc.player.getPosition().add(0, 1,
                    0)).getBlock() instanceof BlockAir && mc.gameSettings.keyBindJump.isKeyDown()) {
                tick++;
                mc.player.motionY = .12f;
            }
            if (hasEnchantments) {
                tick++;
                MoveUtility.setMotion(0.4f);
                StrafeModule.oldSpeed = 0.4f;
            }

        }

    };

    @Override
    protected void onDisable() {
        tick = 0;
        super.onDisable();
    }

    public boolean buildEnchantments(ItemStack stack, float strenght) {
        if (stack != null) {
            if (stack.getItem() instanceof ItemArmor) {
                return EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) > 0;
            }
        } else {
            return false;
        }

        return false;
    }
}
